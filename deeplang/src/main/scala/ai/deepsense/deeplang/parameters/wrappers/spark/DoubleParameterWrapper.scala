package ai.deepsense.deeplang.parameters.wrappers.spark

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.NumericParameter
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.validators.Validator

class DoubleParameterWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.DoubleParam,
    override val validator: Validator[Double] = RangeValidator(Double.MinValue, Double.MaxValue)
) extends NumericParameter(name, description, validator)
    with ForwardSparkParameterWrapper[P, Double] {

  override def replicate(name: String): DoubleParameterWrapper[P] =
    new DoubleParameterWrapper[P](name, description, sparkParamGetter, validator)

}
