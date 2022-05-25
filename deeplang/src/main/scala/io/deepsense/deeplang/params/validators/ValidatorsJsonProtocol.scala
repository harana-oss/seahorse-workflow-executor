package io.deepsense.deeplang.params.validators

import scala.util.matching.Regex

import spray.json._

object ValidatorsJsonProtocol extends DefaultJsonProtocol {
  implicit object RegexJsonFormat extends JsonFormat[Regex] {
    override def write(regex: Regex): JsValue = regex.toString.toJson

    /**
     * This method is not implemented on purpose - RegexJsonFormat is only needed
     * for writing inside [[regexValidatorFormat]].
     */
    override def read(json: JsValue): Regex = ???
  }

  implicit val rangeValidatorFormat = jsonFormat(
    RangeValidator.apply, "begin", "end", "beginIncluded", "endIncluded", "step")

  implicit val regexValidatorFormat = jsonFormat(RegexValidator, "regex")

  implicit val arrayLengthValidator = jsonFormat(ArrayLengthValidator.apply, "min", "max")

  implicit val complexArrayValidator = new JsonFormat[ComplexArrayValidator] {
    def write(v: ComplexArrayValidator): JsValue = {
      v.rangeValidator.configurationToJson
    }
    def read(json: JsValue): ComplexArrayValidator = ???
  }
//  TODO DS-3225 Complex Array Validator serialization
//  implicit val complexArrayValidator =
//    jsonFormat(ComplexArrayValidator.apply, "rangeValidator", "arrayLengthValidator")
}
