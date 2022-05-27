package ai.deepsense.deeplang.parameters

import spray.json.DefaultJsonProtocol.StringJsonFormat

import ai.deepsense.deeplang.exceptions.DeepLangException
import ai.deepsense.deeplang.parameters.validators.ColumnPrefixNameValidator

case class PrefixBasedColumnCreatorParameter(override val name: String, override val description: Option[String])
    extends ParameterWithJsFormat[String] {

  override def validate(value: String): Vector[DeepLangException] =
    ColumnPrefixNameValidator.validate(name, value) ++ super.validate(value)

  val parameterType = ParameterType.PrefixBasedColumnCreator

  override def replicate(name: String): PrefixBasedColumnCreatorParameter = copy(name = name)

}

trait EmptyPrefixValidator extends PrefixBasedColumnCreatorParameter {

  override def validate(value: String): Vector[DeepLangException] = {
    if (value.isEmpty)
      Vector()
    else
      super.validate(value)
  }

}
