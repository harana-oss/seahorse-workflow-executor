package ai.deepsense.deeplang.actions.exceptions

case class SchemaMismatchException(override val message: String)
    extends ActionExecutionException(s"Schema mismatch: $message", None)
