package io.deepsense.graph.nodestate.name

sealed trait NodeStatusName

object NodeStatusName {

  case object Queued extends NodeStatusName

  case object Draft extends NodeStatusName

  case object Running extends NodeStatusName

  case object Completed extends NodeStatusName

  case object Aborted extends NodeStatusName

  case object Failed extends NodeStatusName

}
