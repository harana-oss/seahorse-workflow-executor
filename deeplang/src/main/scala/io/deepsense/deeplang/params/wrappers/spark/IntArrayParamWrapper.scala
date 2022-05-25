package io.deepsense.deeplang.params.wrappers.spark

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import io.deepsense.deeplang.params.MultipleNumericParam
import io.deepsense.deeplang.params.validators.{ComplexArrayValidator, RangeValidator, Validator}

class IntArrayParamWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.IntArrayParam,
    override val validator: Validator[Array[Double]] =
      ComplexArrayValidator(
        RangeValidator(Int.MinValue, Int.MaxValue, step = Some(1.0))))
  extends MultipleNumericParam(name, description, validator)
  with SparkParamWrapper[P, Array[Int], Array[Double]] {

  import IntArrayParamWrapper._

  require(arrayValidatorHasMinMaxInIntegerRange(validator))
  require(arrayValidatorHasIntegerStep(validator))

  override def convert(values: Array[Double])(schema: StructType): Array[Int] =
    values.map(value => value.toInt)

  override def replicate(name: String): IntArrayParamWrapper[P] =
    new IntArrayParamWrapper[P](name, description, sparkParamGetter, validator)
}

object IntArrayParamWrapper {

  def arrayValidatorHasMinMaxInIntegerRange(validator: Validator[Array[Double]]): Boolean = {
    import IntParamWrapper.validatorHasMinMaxInIntegerRange

    validator match {
      case ComplexArrayValidator(rangeValidator, arrayLengthValidator) =>
        validatorHasMinMaxInIntegerRange(rangeValidator)
    }
  }

  def arrayValidatorHasIntegerStep(validator: Validator[Array[Double]]): Boolean = {
    import IntParamWrapper.validatorHasIntegerStep

    validator match {
    case ComplexArrayValidator(rangeValidator, arrayLengthValidator) =>
      validatorHasIntegerStep(rangeValidator)
    }
  }
}
