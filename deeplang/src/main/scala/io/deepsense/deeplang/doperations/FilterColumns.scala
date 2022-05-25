package io.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.ColumnsFilterer

class FilterColumns extends TransformerAsOperation[ColumnsFilterer] with OperationDocumentation {

  override val id: Id = "6534f3f4-fa3a-49d9-b911-c213d3da8b5d"
  override val name: String = "Filter Columns"
  override val description: String =
    "Creates a DataFrame containing only selected columns"

  override lazy val tTagTO_1: TypeTag[ColumnsFilterer] = typeTag

  override val since: Version = Version(0, 4, 0)
}
