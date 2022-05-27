package ai.deepsense.deeplang.actions

import scala.reflect.runtime.universe.TypeTag

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.MissingValuesHandler

class HandleMissingValues extends TransformerAsOperation[MissingValuesHandler] with OperationDocumentation {

  override val id: Id = "d5f4e717-429f-4a28-a0d3-eebba036363a"

  override val name: String = "Handle Missing Values"

  override val description: String =
    """Handles missing values in a DataFrame.
      |In numeric column NaNs are considered as missing values.
    """.stripMargin

  override lazy val tTagTO_1: TypeTag[MissingValuesHandler] = typeTag

  override val since: Version = Version(0, 4, 0)

}
