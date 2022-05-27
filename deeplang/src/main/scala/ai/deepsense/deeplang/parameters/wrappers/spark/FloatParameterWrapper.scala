package ai.deepsense.deeplang.parameters.wrappers.spark

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.parameters.NumericParameter
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.validators.Validator

class FloatParameterWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.FloatParam,
    override val validator: Validator[Double] = RangeValidator(Float.MinValue, Float.MaxValue)
) extends NumericParameter(name, description, validator)
    with SparkParameterWrapper[P, Float, Double] {

  override def convert(value: Double)(schema: StructType): Float = value.toFloat

  override def replicate(name: String): FloatParameterWrapper[P] =
    new FloatParameterWrapper[P](name, description, sparkParamGetter, validator)

}
