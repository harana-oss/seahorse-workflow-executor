package ai.deepsense.deeplang.parameters.exceptions

case class OutOfRangeException(name: String, value: Double, lowerBound: Double, upperBound: Double)
    extends ValidationException(
      s"Parameter '$name' value is out of range. " +
        s"Value `$value` is not in [$lowerBound; $upperBound]"
    )
