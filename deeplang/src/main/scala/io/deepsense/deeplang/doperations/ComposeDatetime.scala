package io.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.DatetimeComposer

class ComposeDatetime extends TransformerAsOperation[DatetimeComposer] with OperationDocumentation {

  override val id: Id = "291cdd16-b57a-4613-abbe-3fd73011e579"
  override val name: String = "Compose Datetime"
  override val description: String =
    "Combines Numeric fields (year, month, etc.) into a Timestamp column"

  override lazy val tTagTO_1: TypeTag[DatetimeComposer] = typeTag

  override val since: Version = Version(1, 3, 0)
}
