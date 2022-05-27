package ai.deepsense.deeplang.actions.layout

import ai.deepsense.deeplang.PortPosition.PortPosition
import ai.deepsense.deeplang._

sealed trait SmallBlockLayout {

  protected val symmetricPortLayout =
    Vector(PortPosition.Left, PortPosition.Right)

}

trait SmallBlockLayout2To0 extends SmallBlockLayout {
  self: Action2To0[_, _] =>

  override def inPortsLayout: Vector[PortPosition] = symmetricPortLayout

}

trait SmallBlockLayout2To1 extends SmallBlockLayout {
  self: Action2To1[_, _, _] =>

  override def inPortsLayout: Vector[PortPosition] = symmetricPortLayout

}

trait SmallBlockLayout2To2 extends SmallBlockLayout {
  self: Action2To2[_, _, _, _] =>

  override def inPortsLayout: Vector[PortPosition] = symmetricPortLayout

  override def outPortsLayout: Vector[PortPosition] = symmetricPortLayout

}

trait SmallBlockLayout2To3 extends SmallBlockLayout {
  self: Action2To3[_, _, _, _, _] =>

  override def inPortsLayout: Vector[PortPosition] = symmetricPortLayout

}

trait SmallBlockLayout0To2 extends SmallBlockLayout {
  self: Action0To2[_, _] =>

  override def outPortsLayout: Vector[PortPosition] = symmetricPortLayout

}

trait SmallBlockLayout1To2 extends SmallBlockLayout {
  self: Action1To2[_, _, _] =>

  override def outPortsLayout: Vector[PortPosition] = symmetricPortLayout

}

trait SmallBlockLayout3To2 extends SmallBlockLayout {
  self: Action3To2[_, _, _, _, _] =>

  override def outPortsLayout: Vector[PortPosition] = symmetricPortLayout

}
