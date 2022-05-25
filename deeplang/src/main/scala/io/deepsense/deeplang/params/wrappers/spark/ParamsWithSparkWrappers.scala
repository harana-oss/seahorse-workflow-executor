package io.deepsense.deeplang.params.wrappers.spark

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import io.deepsense.deeplang.params.Params

trait ParamsWithSparkWrappers extends Params {
  lazy val sparkParamWrappers: Array[SparkParamWrapper[_, _, _]] = params.collect {
    case wrapper: SparkParamWrapper[_, _, _] => wrapper +: wrapper.nestedWrappers
  }.flatten

  protected def validateSparkEstimatorParams(
      sparkEntity: ml.param.Params,
      maybeSchema: Option[StructType]): Unit = {
    maybeSchema.foreach(schema => sparkParamMap(sparkEntity, schema))
  }

  /**
    * This method extracts Spark parameters from SparkParamWrappers that are:
    * - declared directly in class which mixes this trait in
    * - declared in values of parameters (i.e. ChoiceParam, MultipleChoiceParam)
    */
  def sparkParamMap(sparkEntity: ml.param.Params, schema: StructType): ml.param.ParamMap = {

    val directParamMap = ml.param.ParamMap(
      sparkParamWrappers.flatMap(wrapper =>
        getOrDefaultOption(wrapper).map(value => {
          val convertedValue = wrapper.convertAny(value)(schema)
          ml.param.ParamPair(
            wrapper.sparkParam(sparkEntity).asInstanceOf[ml.param.Param[Any]], convertedValue)
        })
      ): _*)

    val paramsNestedInParamValues = params.flatMap(param => {
      get(param) match {
        case Some(nestedParams: ParamsWithSparkWrappers) =>
          Some(nestedParams.sparkParamMap(sparkEntity, schema))
        case _ => None
      }
    }).foldLeft(ml.param.ParamMap())((map1, map2) => map1 ++ map2)

    directParamMap ++ paramsNestedInParamValues
  }

}
