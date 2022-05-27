package ai.deepsense.deeplang.parameters

import java.lang.reflect.Modifier

import spray.json._

import ai.deepsense.commons.utils.CollectionExtensions._
import ai.deepsense.commons.utils.Logging
import ai.deepsense.deeplang.actionobjects.descriptions.HasInferenceResult
import ai.deepsense.deeplang.actionobjects.descriptions.ParamsInferenceResult
import ai.deepsense.deeplang.exceptions.DeepLangException
import ai.deepsense.deeplang.exceptions.DeepLangMultiException
import ai.deepsense.deeplang.parameters.exceptions.ParamValueNotProvidedException
import ai.deepsense.deeplang.parameters.multivalue.MultipleValuesParam
import ai.deepsense.deeplang.parameters.wrappers.spark._
import scala.reflect.runtime.{universe => ru}

import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

/** Everything that inherits this trait declares that it contains parameters. Parameters are discovered by reflection.
  * This trait also provides method for managing values and default values of parameters.
  */
trait Params extends Serializable with HasInferenceResult with DefaultJsonProtocol with Logging {

  def paramsToJson: JsValue = JsArray(params.map { case param =>
    val default = getDefault(param)
    param.toJson(default)
  }: _*)

  /** Json describing values associated to parameters. */
  def paramValuesToJson: JsValue = {
    val fields = for (param <- params) yield get(param).map { case paramValue =>
      param.name -> param.anyValueToJson(paramValue)
    }
    JsObject(fields.flatten.toMap)
  }

  /** Sequence of paramPairs for this class, parsed from Json. If a name of a parameter is unknown, it's ignored JsNull
    * is treated as empty object. JsNull as value of parameter is ignored.
    */
  def paramPairsFromJson(jsValue: JsValue, graphReader: GraphReader): Seq[ParamPair[_]] = jsValue match {
    case JsObject(map) =>
      val pairs = for ((label, value) <- map) yield {
        (paramsByName.get(label), value) match {
          case (Some(parameter), JsNull) => None
          case (Some(parameter), _)      =>
            if (isMultiValueParam(parameter, value))
              getMultiValueParam(value, parameter)
            else
              Some(ParamPair(parameter.asInstanceOf[Parameter[Any]], parameter.valueFromJson(value, graphReader)))
          case (None, _)                 =>
            // Currently frontend might occasionally send invalid params
            // (like removing public param from custom transformer or DS-2671)
            // In that case we are doing nothing.
            logger.warn(s"Field $label is not defined in schema. Ignoring...")
            None
        }
      }
      pairs.flatten.toSeq
    case JsNull        => Seq.empty
    case _             => throw objectExpectedException(jsValue)
  }

  /** Sequence of params without values for this class, parsed from Json. If a name of a parameter is unknown, it's
    * ignored JsNull is treated as empty object. JsNull as a value of a parameter unsets param's value.
    */
  def noValueParamsFromJson(jsValue: JsValue): Seq[Parameter[_]] = jsValue match {
    case JsObject(map) =>
      val pairs = for ((label, value) <- map) yield {
        (paramsByName.get(label), value) match {
          case (p @ Some(parameter), JsNull) => p
          case (Some(parameter), _)          => None
          case (None, _)                     =>
            // Currently frontend might occasionally send invalid params
            // (like removing public param from custom transformer or DS-2671)
            // In that case we are doing nothing.
            logger.info(s"Field $label is not defined in schema. Ignoring...")
            None
        }
      }
      pairs.flatten.toSeq
    case JsNull        => Seq.empty
    case _             => throw objectExpectedException(jsValue)
  }

  private def getMultiValueParam(value: JsValue, parameter: Parameter[_]): Option[ParamPair[_]] = {
    parameter match {
      case _: IntParameterWrapper[_] | _: LongParameterWrapper[_] | _: FloatParameterWrapper[_] | _: DoubleParameterWrapper[_] |
           _: NumericParameter =>
        createMultiValueParam[Double](value, parameter)
      case _ => None
    }
  }

  private def isMultiValueParam(parameter: Parameter[_], value: JsValue): Boolean =
    parameter.isGriddable && MultipleValuesParam.isMultiValParam(value)

  private def createMultiValueParam[T](value: JsValue, parameter: Parameter[_])(implicit
                                                                                format: JsonFormat[T]
  ): Option[ParamPair[T]] = {
    val multiValParam = MultipleValuesParam.fromJson[T](value)
    Some(ParamPair(parameter.asInstanceOf[Parameter[T]], multiValParam.values))
  }

  /** Sets param values based on provided json. If a name of a parameter is unknown, it's ignored JsNull is treated as
    * empty object.
    *
    * When ignoreNulls = false, JsNull as a value of a parameter unsets param's value. When ignoreNulls = true,
    * parameters with JsNull values are ignored.
    */
  def setParamsFromJson(jsValue: JsValue, graphReader: GraphReader, ignoreNulls: Boolean = false): this.type = {
    set(paramPairsFromJson(jsValue, graphReader): _*)
    if (!ignoreNulls)
      noValueParamsFromJson(jsValue).foreach(clear)
    this
  }

  def params: Array[Parameter[_]]

  private lazy val paramsByName: Map[String, Parameter[_]] =
    params.map { case param => param.name -> param }.toMap

  protected def customValidateParams: Vector[DeepLangException] = Vector.empty

  /** Validates params' values by:
    *   1. testing whether the params have values set (or default values), 2. testing whether the values meet the
    *      constraints, 3. testing custom validations, possibly spanning over multiple params.
    */
  def validateParams: Vector[DeepLangException] = {
    val singleParameterErrors  = params.flatMap { param =>
      if (isDefined(param)) {
        val paramValue: Any          = $(param)
        val anyTypeParam: Parameter[Any] = param.asInstanceOf[Parameter[Any]]
        anyTypeParam.validate(paramValue)
      } else
        Vector(new ParamValueNotProvidedException(param.name))
    }.toVector
    val customValidationErrors = customValidateParams
    singleParameterErrors ++ customValidationErrors
  }

  /** Validates Params entities that contain dynamic parameters' values. Validation errors are wrapped in
    * DeepLangMultiException.
    */
  def validateDynamicParams(params: Params*): Unit = {
    val validationResult = params.flatMap(param => param.validateParams).toVector
    if (validationResult.nonEmpty)
      throw DeepLangMultiException(validationResult)
  }

  final def isSet(param: Parameter[_]): Boolean =
    paramMap.contains(param)

  final def isDefined(param: Parameter[_]): Boolean =
    defaultParamMap.contains(param) || paramMap.contains(param)

  private def hasParam(paramName: String): Boolean =
    params.exists(_.name == paramName)

  private def getParam(paramName: String): Parameter[Any] = {
    params
      .find(_.name == paramName)
      .getOrElse {
        throw new NoSuchElementException(s"Param $paramName does not exist.")
      }
      .asInstanceOf[Parameter[Any]]
  }

  final protected def set[T](param: Parameter[T], value: T): this.type =
    set(param -> value)

  final private def set(param: String, value: Any): this.type =
    set(getParam(param), value)

  final protected[deeplang] def set(paramPair: ParamPair[_]): this.type = {
    paramMap.put(paramPair)
    this
  }

  final protected[deeplang] def set(paramPairs: ParamPair[_]*): this.type = {
    paramMap.put(paramPairs: _*)
    this
  }

  final protected[deeplang] def set(paramMap: ParameterMap): this.type = {
    set(paramMap.toSeq: _*)
    this
  }

  final protected def clear(param: Parameter[_]): this.type = {
    paramMap.remove(param)
    this
  }

  final def get[T](param: Parameter[T]): Option[T] = paramMap.get(param)

  final def getOrDefaultOption[T](param: Parameter[T]): Option[T] = get(param).orElse(getDefault(param))

  final def getOrDefault[T](param: Parameter[T]): T = getOrDefaultOption(param).getOrElse {
    throw ParamValueNotProvidedException(param.name)
  }

  final protected def $[T](param: Parameter[T]): T = getOrDefault(param)

  protected def setDefault[T](param: Parameter[T], value: T): this.type = {
    defaultParamMap.put(param -> value)
    this
  }

  protected def setDefault(paramPairs: ParamPair[_]*): this.type = {
    paramPairs.foreach(p => setDefault(p.param.asInstanceOf[Parameter[Any]], p.value))
    this
  }

  final def getDefault[T](param: Parameter[T]): Option[T] =
    defaultParamMap.get(param)

  final def hasDefault[T](param: Parameter[T]): Boolean =
    defaultParamMap.contains(param)

  final def extractParamMap(extra: ParameterMap = ParameterMap.empty): ParameterMap =
    defaultParamMap ++ paramMap ++ extra

  def replicate(extra: ParameterMap = ParameterMap.empty): this.type = {
    val that = this.getClass.getConstructor().newInstance().asInstanceOf[this.type]
    copyValues(that, extra)
  }

  /** Compares 'this' and 'other' params. Objects are equal when they are of the same class and their parameters have
    * the same values set.
    * @return
    *   True, if 'this' and 'other' are the same.
    */
  def sameAs(other: Params): Boolean =
    other.getClass == this.getClass && other.paramValuesToJson == this.paramValuesToJson

  // TODO Mutability leakage - it's possible to mutate object `to` internals from outside.
  // there should be protected copyFrom from instead (not `to`).
  def copyValues[T <: Params](to: T, extra: ParameterMap = ParameterMap.empty): T = {
    val map = paramMap ++ extra
    params.foreach { param =>
      if (map.contains(param) && to.hasParam(param.name))
        to.set(param.name, map(param))
    }
    to
  }

  override def inferenceResult: Option[ParamsInferenceResult] = {
    Some(
      ParamsInferenceResult(
        schema = paramsToJson,
        values = paramValuesToJson
      )
    )
  }

  private def objectExpectedException(jsValue: JsValue): DeserializationException =
    new DeserializationException(s"Cannot fill parameters schema with $jsValue object expected.")

  private[deeplang] def paramMap: ParameterMap = _paramMap

  private val _paramMap: ParameterMap = ParameterMap.empty

  private[deeplang] def defaultParamMap: ParameterMap = _defaultParamMap

  private val _defaultParamMap: ParameterMap = ParameterMap.empty

}
