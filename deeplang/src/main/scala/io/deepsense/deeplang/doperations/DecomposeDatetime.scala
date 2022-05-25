package io.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.DatetimeDecomposer

class DecomposeDatetime extends TransformerAsOperation[DatetimeDecomposer] with OperationDocumentation  {

  override val id: Id = "6c18b05e-7db7-4315-bce1-3291ed530675"
  override val name: String = "Decompose Datetime"
  override val description: String =
    "Extracts Numeric fields (year, month, etc.) from a Timestamp column"

  override lazy val tTagTO_1: TypeTag[DatetimeDecomposer] = typeTag

  override val since: Version = Version(0, 4, 0)
}
