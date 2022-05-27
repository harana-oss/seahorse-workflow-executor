package ai.deepsense.deeplang.parameters.wrappers.spark

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.parameters.NumericParameter
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.validators.Validator

class LongParameterWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.LongParam,
    // TODO change to full Long range when RangeValidator will be rewritten
    override val validator: Validator[Double] = RangeValidator(Int.MinValue, Int.MaxValue, step = Some(1.0))
) extends NumericParameter(name, description, validator)
    with SparkParameterWrapper[P, Long, Double] {

  require(IntParameterWrapper.validatorHasIntegerStep(validator))

  override def convert(value: Double)(schema: StructType): Long = value.toLong

  override def replicate(name: String): LongParameterWrapper[P] =
    new LongParameterWrapper[P](name, description, sparkParamGetter, validator)

}
