package io.deepsense.deeplang.params

import io.deepsense.deeplang.params.selections.MultipleColumnSelection
import io.deepsense.deeplang.params.selections.MultipleColumnSelectionProtocol._

case class ColumnSelectorParam(
    override val name: String,
    override val description: Option[String],
    portIndex: Int)
  extends AbstractColumnSelectorParam[MultipleColumnSelection] {

  override val parameterType = ParameterType.ColumnSelector
  override val isSingle = false

  override def replicate(name: String): ColumnSelectorParam = copy(name = name)
}
