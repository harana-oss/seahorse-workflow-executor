package ai.deepsense.deeplang.params.exceptions

case class OutOfRangeWithStepException(
    name: String,
    value: Double,
    lowerBound: Double,
    upperBound: Double,
    step: Double
) extends ValidationException(
      s"Parameter '$name' value is out of range. " +
        s"Value `$value` is not in [$lowerBound; $upperBound] with step $step"
    )
