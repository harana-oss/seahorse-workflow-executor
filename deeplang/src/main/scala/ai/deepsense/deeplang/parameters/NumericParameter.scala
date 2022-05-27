package ai.deepsense.deeplang.parameters

import spray.json.DefaultJsonProtocol.DoubleJsonFormat

import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.validators.Validator

case class NumericParameter(
    override val name: String,
    override val description: Option[String],
    validator: Validator[Double] = RangeValidator.all
) extends ParameterWithJsFormat[Double]
    with HasValidator[Double] {

  override val parameterType = ParameterType.Numeric

  override val isGriddable: Boolean = true

  override def replicate(name: String): NumericParameter = copy(name = name)

}
