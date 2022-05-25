package io.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.RowsFilterer

class FilterRows extends TransformerAsOperation[RowsFilterer] with OperationDocumentation {

  override val id: Id = "7d7eddfa-c9be-48c3-bb8c-5f7cc59b403a"
  override val name: String = "Filter Rows"
  override val description: String =
    "Creates a DataFrame containing only rows satisfying given condition"

  override lazy val tTagTO_1: TypeTag[RowsFilterer] = typeTag

  override val since: Version = Version(1, 0, 0)
}
