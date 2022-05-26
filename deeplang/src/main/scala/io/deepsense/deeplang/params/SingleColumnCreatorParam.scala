package io.deepsense.deeplang.params

import spray.json.DefaultJsonProtocol.StringJsonFormat

import io.deepsense.deeplang.params.validators.ColumnNameValidator
import io.deepsense.deeplang.params.validators.Validator

case class SingleColumnCreatorParam(override val name: String, override val description: Option[String])
    extends ParamWithJsFormat[String]
    with HasValidator[String] {

  val validator: Validator[String] = ColumnNameValidator

  val parameterType = ParameterType.SingleColumnCreator

  override def replicate(name: String): SingleColumnCreatorParam = copy(name = name)

}
