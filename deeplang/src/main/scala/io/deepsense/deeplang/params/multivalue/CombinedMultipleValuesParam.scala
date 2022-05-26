package io.deepsense.deeplang.params.multivalue

case class CombinedMultipleValuesParam[T](gridValues: Seq[MultipleValuesParam[T]]) extends MultipleValuesParam[T] {

  override val values: Seq[T] = gridValues.flatMap(_.values).distinct

}
