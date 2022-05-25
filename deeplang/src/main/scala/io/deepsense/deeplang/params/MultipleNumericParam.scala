package io.deepsense.deeplang.params

import spray.json.DefaultJsonProtocol._
import spray.json._

import io.deepsense.deeplang.exceptions.DeepLangException
import io.deepsense.deeplang.params.validators.{ComplexArrayValidator, Validator}

case class MultipleNumericParam(
    override val name: String,
    override val description: Option[String],
    validator: Validator[Array[Double]] = ComplexArrayValidator.all)
  extends Param[Array[Double]] {

  override val parameterType = ParameterType.MultipleNumeric

  def valueToJson(value: Array[Double]): JsValue = {
    JsObject(
      "values" -> JsArray(
        JsObject(
          "type" -> JsString("seq"),
          "value" -> JsObject(
            "sequence" -> value.toJson
          )
        )
      )
    )
  }

  override def valueFromJson(jsValue: JsValue): Array[Double] = jsValue match {
    case JsObject(map) =>
      map("values")
        .asInstanceOf[JsArray].elements(0)
        .asJsObject.fields("value")
        .asJsObject.fields("sequence")
        .convertTo[Array[Double]]
    case _ => throw new DeserializationException(s"Cannot fill choice parameter with $jsValue:" +
      s" object expected.")
  }

  override def replicate(name: String): MultipleNumericParam = copy(name = name)

  override protected def extraJsFields: Map[String, JsValue] =
    super.extraJsFields ++ Map("validator" -> validator.toJson)

  override def validate(values: Array[Double]): Vector[DeepLangException] = {
    validator.validate(name, values)
  }
}
