package ai.deepsense.deeplang.parameters

case class ParamPair[T](param: Parameter[T], values: Seq[T]) {

  require(values.nonEmpty)
  values.foreach(param.validate)

  lazy val value = values.head

}

object ParamPair {

  def apply[T](param: Parameter[T], value: T): ParamPair[T] =
    ParamPair(param, Seq(value))

}
