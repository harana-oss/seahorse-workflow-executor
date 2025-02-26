package ai.deepsense.deeplang.parameters.exceptions

case class ArrayTooShort(name: String, arrayLength: Int, minLength: Int)
    extends ValidationException(
      s"Array '$name' is too short. " +
        s"Length of `$name` is `$arrayLength` but needs to be at least `$minLength`."
    )
