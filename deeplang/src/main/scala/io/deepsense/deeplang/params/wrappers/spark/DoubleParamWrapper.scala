package io.deepsense.deeplang.params.wrappers.spark

import org.apache.spark.ml

import io.deepsense.deeplang.params.NumericParam
import io.deepsense.deeplang.params.validators.{RangeValidator, Validator}

class DoubleParamWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.DoubleParam,
    override val validator: Validator[Double] = RangeValidator(Double.MinValue, Double.MaxValue))
  extends NumericParam(name, description, validator)
  with ForwardSparkParamWrapper[P, Double] {

  override def replicate(name: String): DoubleParamWrapper[P] =
    new DoubleParamWrapper[P](name, description, sparkParamGetter, validator)
}
