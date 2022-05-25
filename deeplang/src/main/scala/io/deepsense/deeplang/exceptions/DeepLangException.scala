package io.deepsense.deeplang.exceptions

import io.deepsense.commons.exception.{FailureDescription, FailureCode, DeepSenseException}

class DeepLangException(
    override val message: String,
    cause: Throwable = null)
  extends DeepSenseException(
    FailureCode.NodeFailure,
    "DeepLang Exception",
    message,
    Option(cause),
    Option(cause)
      .map(e => FailureDescription.stacktraceDetails(e.getStackTrace))
      .getOrElse(Map())) {

  def toVector: Vector[DeepLangException] = Vector(this)
}
