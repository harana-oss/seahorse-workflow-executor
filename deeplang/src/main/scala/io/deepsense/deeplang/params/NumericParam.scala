package io.deepsense.deeplang.params

import spray.json.DefaultJsonProtocol.DoubleJsonFormat

import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.validators.Validator

case class NumericParam(
    override val name: String,
    override val description: Option[String],
    validator: Validator[Double] = RangeValidator.all
) extends ParamWithJsFormat[Double]
    with HasValidator[Double] {

  override val parameterType = ParameterType.Numeric

  override val isGriddable: Boolean = true

  override def replicate(name: String): NumericParam = copy(name = name)

}
