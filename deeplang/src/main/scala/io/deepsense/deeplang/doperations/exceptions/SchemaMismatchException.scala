package io.deepsense.deeplang.doperations.exceptions

case class SchemaMismatchException(override val message: String)
  extends DOperationExecutionException(s"Schema mismatch: $message", None)
