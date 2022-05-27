package ai.deepsense.deeplang.actions

import scala.reflect.runtime.universe.TypeTag

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.RowsFilterer

class FilterRows extends TransformerAsOperation[RowsFilterer] with OperationDocumentation {

  override val id: Id = "7d7eddfa-c9be-48c3-bb8c-5f7cc59b403a"

  override val name: String = "Filter Rows"

  override val description: String =
    "Creates a DataFrame containing only rows satisfying given condition"

  override lazy val tTagTO_1: TypeTag[RowsFilterer] = typeTag

  override val since: Version = Version(1, 0, 0)

}
