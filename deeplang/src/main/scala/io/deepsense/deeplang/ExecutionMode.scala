package io.deepsense.deeplang

sealed trait ExecutionMode
object ExecutionMode {
  case object Batch extends ExecutionMode
  case object Interactive extends ExecutionMode
}
