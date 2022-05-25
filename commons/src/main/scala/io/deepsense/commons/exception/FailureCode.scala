package io.deepsense.commons.exception

object FailureCode extends Enumeration {
  type FailureCode = Value

  val NodeFailure = Value(1)
  val LaunchingFailure = Value(2)
  val WorkflowNotFound = Value(3)
  val CannotUpdateRunningWorkflow = Value(4)
  val EntityNotFound = Value(5)
  val UnexpectedError = Value(6)
  val IllegalArgumentException = Value(7)
  val IncorrectWorkflow = Value(8)
  val IncorrectNode = Value(9)

  def fromCode(code: Int): Option[FailureCode] = FailureCode.values.find(_.id == code)
}
