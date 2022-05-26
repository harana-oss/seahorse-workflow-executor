package io.deepsense.deeplang.params.wrappers.spark

import org.apache.spark.ml

import io.deepsense.deeplang.params.MultipleNumericParam
import io.deepsense.deeplang.params.validators.ArrayLengthValidator
import io.deepsense.deeplang.params.validators.ComplexArrayValidator
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.validators.Validator

class DoubleArrayParamWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.DoubleArrayParam,
    override val validator: Validator[Array[Double]] =
      ComplexArrayValidator(rangeValidator = RangeValidator.all, lengthValidator = ArrayLengthValidator.withAtLeast(1))
) extends MultipleNumericParam(name, description, validator)
    with ForwardSparkParamWrapper[P, Array[Double]] {

  override def replicate(name: String): DoubleArrayParamWrapper[P] =
    new DoubleArrayParamWrapper[P](name, description, sparkParamGetter, validator)

}
