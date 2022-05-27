package ai.deepsense.deeplang.parameters

import ai.deepsense.deeplang.parameters.selections.MultipleColumnSelection
import ai.deepsense.deeplang.parameters.selections.MultipleColumnSelectionProtocol._

case class ColumnSelectorParameter(override val name: String, override val description: Option[String], portIndex: Int)
    extends AbstractColumnSelectorParameter[MultipleColumnSelection] {

  override val parameterType = ParameterType.ColumnSelector

  override val isSingle = false

  override def replicate(name: String): ColumnSelectorParameter = copy(name = name)

}
