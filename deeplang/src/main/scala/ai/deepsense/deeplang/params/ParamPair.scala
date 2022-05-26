package ai.deepsense.deeplang.params

case class ParamPair[T](param: Param[T], values: Seq[T]) {

  require(values.nonEmpty)
  values.foreach(param.validate)

  lazy val value = values.head

}

object ParamPair {

  def apply[T](param: Param[T], value: T): ParamPair[T] =
    ParamPair(param, Seq(value))

}
