package ai.deepsense.deeplang.parameters

import ai.deepsense.deeplang.parameters.selections.SingleColumnSelection
import ai.deepsense.deeplang.parameters.selections.SingleColumnSelectionProtocol
import ai.deepsense.deeplang.parameters.selections.SingleColumnSelectionProtocol._

case class SingleColumnSelectorParameter(
    override val name: String,
    override val description: Option[String],
    portIndex: Int
) extends AbstractColumnSelectorParameter[SingleColumnSelection]
    with SingleColumnSelectionProtocol {

  override val parameterType = ParameterType.ColumnSelector

  override val isSingle = true

  override def replicate(name: String): SingleColumnSelectorParameter = copy(name = name)

}
