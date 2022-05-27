package ai.deepsense.deeplang.parameters.wrappers.spark

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.parameters.MultipleNumericParameter
import ai.deepsense.deeplang.parameters.validators.ComplexArrayValidator
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.validators.Validator

class IntArrayParameterWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.IntArrayParam,
    override val validator: Validator[Array[Double]] = ComplexArrayValidator(
      RangeValidator(Int.MinValue, Int.MaxValue, step = Some(1.0))
    )
) extends MultipleNumericParameter(name, description, validator)
    with SparkParameterWrapper[P, Array[Int], Array[Double]] {

  import IntArrayParameterWrapper._

  require(arrayValidatorHasMinMaxInIntegerRange(validator))
  require(arrayValidatorHasIntegerStep(validator))

  override def convert(values: Array[Double])(schema: StructType): Array[Int] =
    values.map(value => value.toInt)

  override def replicate(name: String): IntArrayParameterWrapper[P] =
    new IntArrayParameterWrapper[P](name, description, sparkParamGetter, validator)

}

object IntArrayParameterWrapper {

  def arrayValidatorHasMinMaxInIntegerRange(validator: Validator[Array[Double]]): Boolean = {
    import IntParameterWrapper.validatorHasMinMaxInIntegerRange

    validator match {
      case ComplexArrayValidator(rangeValidator, arrayLengthValidator) =>
        validatorHasMinMaxInIntegerRange(rangeValidator)
    }
  }

  def arrayValidatorHasIntegerStep(validator: Validator[Array[Double]]): Boolean = {
    import IntParameterWrapper.validatorHasIntegerStep

    validator match {
      case ComplexArrayValidator(rangeValidator, arrayLengthValidator) =>
        validatorHasIntegerStep(rangeValidator)
    }
  }

}
