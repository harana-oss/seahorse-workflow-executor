package io.deepsense.deeplang.params

import io.deepsense.deeplang.params.selections.SingleColumnSelection
import io.deepsense.deeplang.params.selections.SingleColumnSelectionProtocol
import io.deepsense.deeplang.params.selections.SingleColumnSelectionProtocol._

case class SingleColumnSelectorParam(
    override val name: String,
    override val description: Option[String],
    portIndex: Int
) extends AbstractColumnSelectorParam[SingleColumnSelection]
    with SingleColumnSelectionProtocol {

  override val parameterType = ParameterType.ColumnSelector

  override val isSingle = true

  override def replicate(name: String): SingleColumnSelectorParam = copy(name = name)

}
