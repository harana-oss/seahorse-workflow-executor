package io.deepsense.deeplang.exceptions

/**
  * This exception is used when we need to throw multiple validation errors at once.
  */
case class DeepLangMultiException(exceptions: Vector[DeepLangException])
  extends DeepLangException("Multiple errors") {

  override def toVector: Vector[DeepLangException] = exceptions
}
