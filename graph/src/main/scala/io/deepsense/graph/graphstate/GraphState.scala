package io.deepsense.graph.graphstate

import io.deepsense.commons.exception.FailureDescription
import io.deepsense.graph.graphstate.GraphState.Names

sealed abstract class GraphState(val name: String) extends Serializable {

  def isDraft: Boolean = false

  def isRunning: Boolean = false

  def isCompleted: Boolean = false

  def isAborted: Boolean = false

  def isFailed: Boolean = false

}

case object Draft extends GraphState(Names.draft) {

  override def isDraft: Boolean = true

}

case object Running extends GraphState(Names.running) {

  override def isRunning: Boolean = true

}

case object Completed extends GraphState(Names.completed) {

  override def isCompleted: Boolean = true

}

case object Aborted extends GraphState(Names.aborted) {

  override def isAborted: Boolean = true

}

case class Failed(error: FailureDescription) extends GraphState(Names.failed) {

  override def isFailed: Boolean = true

}

object GraphState {

  def fromString: PartialFunction[String, GraphState] = {
    case Names.draft     => Draft
    case Names.completed => Completed
    case Names.aborted   => Aborted
    case Names.running   => Running
  }

  def failedFromString(error: => FailureDescription): PartialFunction[String, GraphState] = { case Names.failed =>
    Failed(error)
  }

  def fromString(error: => FailureDescription): PartialFunction[String, GraphState] =
    fromString.orElse(failedFromString(error))

  object Names {

    val draft = "DRAFT"

    val running = "RUNNING"

    val completed = "COMPLETED"

    val aborted = "ABORTED"

    val failed = "FAILED"

  }

}
