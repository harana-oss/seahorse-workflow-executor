package io.deepsense.deeplang.params.exceptions

case class ArrayTooLong(name: String, arrayLength: Int, maxLength: Int)
  extends ValidationException(s"Array '$name' is too long. " +
    s"Length of `$name` is `$arrayLength` but needs to be at most `$maxLength`.")
