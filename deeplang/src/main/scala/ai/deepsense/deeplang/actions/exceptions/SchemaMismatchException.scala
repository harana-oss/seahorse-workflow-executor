package ai.deepsense.deeplang.actions.exceptions

case class SchemaMismatchException(override val message: String)
    extends DOperationExecutionException(s"Schema mismatch: $message", None)
