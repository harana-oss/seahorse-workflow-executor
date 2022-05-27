package ai.deepsense.deeplang.actions

import scala.reflect.runtime.universe.TypeTag

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.DatetimeComposer

class ComposeDatetime extends TransformerAsOperation[DatetimeComposer] with OperationDocumentation {

  override val id: Id = "291cdd16-b57a-4613-abbe-3fd73011e579"

  override val name: String = "Compose Datetime"

  override val description: String =
    "Combines Numeric fields (year, month, etc.) into a Timestamp column"

  override lazy val tTagTO_1: TypeTag[DatetimeComposer] = typeTag

  override val since: Version = Version(1, 3, 0)

}
