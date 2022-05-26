package io.deepsense.graph.nodestate

import org.joda.time.DateTime

import io.deepsense.commons.datetime.DateTimeConverter
import io.deepsense.commons.exception.FailureDescription
import io.deepsense.commons.models.Entity
import io.deepsense.commons.models.Entity.Id
import io.deepsense.graph.nodestate.name.NodeStatusName

sealed abstract class NodeStatus(val name: NodeStatusName, val results: Seq[Entity.Id]) extends Serializable {

  def start: Running

  def finish(results: Seq[Id]): Completed

  def fail(error: FailureDescription): Failed

  def abort: Aborted

  def enqueue: Queued

  // Sugar.

  def isDraft: Boolean = this match {
    case Draft(_) => true
    case _        => false
  }

  def isQueued: Boolean = this match {
    case Queued(_) => true
    case _         => false
  }

  def isRunning: Boolean = this match {
    case Running(_, _) => true
    case _             => false
  }

  def isCompleted: Boolean = this match {
    case Completed(_, _, _) => true
    case _                  => false
  }

  def isAborted: Boolean = this match {
    case Aborted(_) => true
    case _          => false
  }

  def isFailed: Boolean = this match {
    case Failed(_, _, _) => true
    case _               => false
  }

  protected def stateCannot(what: String): IllegalStateException =
    new IllegalStateException(s"State ${this.getClass.getSimpleName} cannot $what()")

}

final case class Draft(override val results: Seq[Entity.Id] = Seq.empty)
    extends NodeStatus(NodeStatusName.Draft, results) {

  override def start: Running = throw stateCannot("start")

  override def finish(results: Seq[Entity.Id]): Completed = throw stateCannot("finish")

  override def abort: Aborted = Aborted(results)

  override def fail(error: FailureDescription): Failed = {
    val now = DateTimeConverter.now
    Failed(now, now, error)
  }

  override def enqueue: Queued = Queued(results)

}

final case class Queued(override val results: Seq[Entity.Id] = Seq.empty)
    extends NodeStatus(NodeStatusName.Queued, results) {

  override def start: Running = Running(DateTimeConverter.now, results)

  override def finish(results: Seq[Entity.Id]): Completed = throw stateCannot("finish")

  override def abort: Aborted = Aborted(results)

  override def fail(error: FailureDescription): Failed = {
    val now = DateTimeConverter.now
    Failed(now, now, error)
  }

  override def enqueue: Queued = throw stateCannot("enqueue")

}

final case class Running(started: DateTime, override val results: Seq[Entity.Id] = Seq.empty)
    extends NodeStatus(NodeStatusName.Running, results) {

  override def start: Running = throw stateCannot("start")

  override def abort: Aborted = Aborted(results)

  override def fail(error: FailureDescription): Failed =
    Failed(started, DateTimeConverter.now, error)

  override def finish(results: Seq[Id]): Completed =
    Completed(started, DateTimeConverter.now, results)

  override def enqueue: Queued = throw stateCannot("enqueue")

}

sealed trait FinalState {
  self: NodeStatus =>

  override def start: Running = throw stateCannot("start")

  override def finish(results: Seq[Id]): Completed = throw stateCannot("finish")

  override def abort: Aborted = throw stateCannot("abort")

  override def fail(error: FailureDescription): Failed = throw stateCannot("fail")

  override def enqueue: Queued = throw stateCannot("enqueue")

}

final case class Completed(started: DateTime, ended: DateTime, override val results: Seq[Id])
    extends NodeStatus(NodeStatusName.Completed, results)
    with FinalState

final case class Failed(started: DateTime, ended: DateTime, error: FailureDescription)
    extends NodeStatus(NodeStatusName.Failed, results = Seq.empty)
    with FinalState

final case class Aborted(override val results: Seq[Entity.Id] = Seq.empty)
    extends NodeStatus(NodeStatusName.Aborted, results)
    with FinalState
