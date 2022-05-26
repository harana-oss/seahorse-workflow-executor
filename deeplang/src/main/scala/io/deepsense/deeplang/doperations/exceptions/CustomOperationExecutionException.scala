package io.deepsense.deeplang.doperations.exceptions

case class CustomOperationExecutionException(override val message: String)
    extends DOperationExecutionException(s"Custom operation execution failed: $message", None)
