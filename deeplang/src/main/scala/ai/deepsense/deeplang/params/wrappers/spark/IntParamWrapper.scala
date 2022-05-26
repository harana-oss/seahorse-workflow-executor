package ai.deepsense.deeplang.params.wrappers.spark

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.params.NumericParam
import ai.deepsense.deeplang.params.validators.RangeValidator
import ai.deepsense.deeplang.params.validators.Validator

class IntParamWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.IntParam,
    override val validator: Validator[Double] = RangeValidator(Int.MinValue, Int.MaxValue, step = Some(1.0))
) extends NumericParam(name, description, validator)
    with SparkParamWrapper[P, Int, Double] {

  import IntParamWrapper._

  require(validatorHasMinMaxInIntegerRange(validator))
  require(validatorHasIntegerStep(validator))

  override def convert(value: Double)(schema: StructType): Int = value.toInt

  override def replicate(name: String): IntParamWrapper[P] =
    new IntParamWrapper[P](name, description, sparkParamGetter, validator)

}

object IntParamWrapper {

  def validatorHasMinMaxInIntegerRange(validator: Validator[Double]): Boolean = {
    validator match {
      case (v: RangeValidator) => inIntegerRange(v.begin) && inIntegerRange(v.end)
      case _                   => true
    }
  }

  private def inIntegerRange(value: Double): Boolean =
    Int.MinValue <= value && value <= Int.MaxValue

  def validatorHasIntegerStep(validator: Validator[Double]): Boolean = {
    validator match {
      case (v: RangeValidator) =>
        v.step match {
          case Some(step) => isInteger(step) && step > 0.0
          case None       => false
        }
      case _                   => true
    }
  }

  private def isInteger(v: Double): Boolean = Math.round(v) == v

}
