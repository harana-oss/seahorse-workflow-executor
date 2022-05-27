package ai.deepsense.deeplang.parameters.wrappers.spark

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.parameters.NumericParameter
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.validators.Validator

class IntParameterWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.IntParam,
    override val validator: Validator[Double] = RangeValidator(Int.MinValue, Int.MaxValue, step = Some(1.0))
) extends NumericParameter(name, description, validator)
    with SparkParameterWrapper[P, Int, Double] {

  import IntParameterWrapper._

  require(validatorHasMinMaxInIntegerRange(validator))
  require(validatorHasIntegerStep(validator))

  override def convert(value: Double)(schema: StructType): Int = value.toInt

  override def replicate(name: String): IntParameterWrapper[P] =
    new IntParameterWrapper[P](name, description, sparkParamGetter, validator)

}

object IntParameterWrapper {

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
