package ai.deepsense.deeplang.actions.exceptions

case class CustomOperationExecutionException(override val message: String)
    extends DOperationExecutionException(s"Custom operation execution failed: $message", None)
