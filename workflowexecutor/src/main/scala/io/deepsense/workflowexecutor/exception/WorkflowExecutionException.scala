package io.deepsense.workflowexecutor.exception

case class WorkflowExecutionException(val cause: Throwable)
    extends Exception(s"Execution failed: ${cause.getMessage}", cause)
