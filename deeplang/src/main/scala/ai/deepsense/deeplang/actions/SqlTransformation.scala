package ai.deepsense.deeplang.actions

import scala.reflect.runtime.universe.TypeTag

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.SqlTransformer

class SqlTransformation extends TransformerAsOperation[SqlTransformer] with OperationDocumentation {

  override val id: Id = "6cba4400-d966-4a2a-8356-b37f37b4c73f"

  override val name: String = "SQL Transformation"

  override val description: String =
    "Executes an SQL transformation on a DataFrame"

  override lazy val tTagTO_1: TypeTag[SqlTransformer] = typeTag

  override val since: Version = Version(0, 4, 0)

}
