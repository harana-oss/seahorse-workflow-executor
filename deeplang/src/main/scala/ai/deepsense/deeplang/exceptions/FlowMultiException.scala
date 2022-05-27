package ai.deepsense.deeplang.exceptions

/** This exception is used when we need to throw multiple validation errors at once. */
case class FlowMultiException(exceptions: Vector[FlowException]) extends FlowException("Multiple errors") {

  override def toVector: Vector[FlowException] = exceptions

}
