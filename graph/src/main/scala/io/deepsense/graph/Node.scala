package io.deepsense.graph

import io.deepsense.commons.models

case class Node[+T](
  id: Node.Id,
  value: T)

object Node {
  type Id = models.Id
  val Id = models.Id
}
