package ai.deepsense.deeplang.params.wrappers.spark

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.params.NumericParam
import ai.deepsense.deeplang.params.validators.RangeValidator
import ai.deepsense.deeplang.params.validators.Validator

class FloatParamWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.FloatParam,
    override val validator: Validator[Double] = RangeValidator(Float.MinValue, Float.MaxValue)
) extends NumericParam(name, description, validator)
    with SparkParamWrapper[P, Float, Double] {

  override def convert(value: Double)(schema: StructType): Float = value.toFloat

  override def replicate(name: String): FloatParamWrapper[P] =
    new FloatParamWrapper[P](name, description, sparkParamGetter, validator)

}
