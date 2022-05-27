package ai.deepsense.deeplang.parameters.wrappers.spark

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.MultipleNumericParameter
import ai.deepsense.deeplang.parameters.validators.ArrayLengthValidator
import ai.deepsense.deeplang.parameters.validators.ComplexArrayValidator
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.validators.Validator

class DoubleArrayParameterWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.DoubleArrayParam,
    override val validator: Validator[Array[Double]] =
      ComplexArrayValidator(rangeValidator = RangeValidator.all, lengthValidator = ArrayLengthValidator.withAtLeast(1))
) extends MultipleNumericParameter(name, description, validator)
    with ForwardSparkParameterWrapper[P, Array[Double]] {

  override def replicate(name: String): DoubleArrayParameterWrapper[P] =
    new DoubleArrayParameterWrapper[P](name, description, sparkParamGetter, validator)

}
