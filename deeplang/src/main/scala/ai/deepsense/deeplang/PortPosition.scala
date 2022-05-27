package ai.deepsense.deeplang

object PortPosition extends Enumeration {

  type PortPosition = Value

  val Left = Value("left")

  val Center = Value("center")

  val Right = Value("right")

}

sealed trait Gravity

case object GravitateLeft  extends Gravity
case object GravitateRight extends Gravity
