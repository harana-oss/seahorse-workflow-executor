package ai.deepsense.deeplang.actions.exceptions

case class CustomOperationExecutionException(override val message: String)
    extends ActionExecutionException(s"Custom operation execution failed: $message", None)
