package ai.deepsense.graph

import ai.deepsense.commons.models

case class Node[+T](id: Node.Id, value: T)

object Node {

  type Id = models.Id

  val Id = models.Id

}
