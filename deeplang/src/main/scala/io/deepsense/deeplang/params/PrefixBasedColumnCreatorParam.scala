package io.deepsense.deeplang.params

import spray.json.DefaultJsonProtocol.StringJsonFormat

import io.deepsense.deeplang.exceptions.DeepLangException
import io.deepsense.deeplang.params.validators.ColumnPrefixNameValidator

case class PrefixBasedColumnCreatorParam(
    override val name: String,
    override val description: Option[String])
  extends ParamWithJsFormat[String] {

  override def validate(value: String): Vector[DeepLangException] = {
    ColumnPrefixNameValidator.validate(name, value) ++ super.validate(value)
  }

  val parameterType = ParameterType.PrefixBasedColumnCreator

  override def replicate(name: String): PrefixBasedColumnCreatorParam = copy(name = name)
}

trait EmptyPrefixValidator extends PrefixBasedColumnCreatorParam {
  override def validate(value: String): Vector[DeepLangException] = {
    if (value.isEmpty) {
      Vector()
    } else {
      super.validate(value)
    }
  }
}
