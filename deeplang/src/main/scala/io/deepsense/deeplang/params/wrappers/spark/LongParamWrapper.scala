package io.deepsense.deeplang.params.wrappers.spark

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import io.deepsense.deeplang.params.NumericParam
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.validators.Validator

class LongParamWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.LongParam,
    // TODO change to full Long range when RangeValidator will be rewritten
    override val validator: Validator[Double] = RangeValidator(Int.MinValue, Int.MaxValue, step = Some(1.0))
) extends NumericParam(name, description, validator)
    with SparkParamWrapper[P, Long, Double] {

  require(IntParamWrapper.validatorHasIntegerStep(validator))

  override def convert(value: Double)(schema: StructType): Long = value.toLong

  override def replicate(name: String): LongParamWrapper[P] =
    new LongParamWrapper[P](name, description, sparkParamGetter, validator)

}
