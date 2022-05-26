package io.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.SqlTransformer

class SqlTransformation extends TransformerAsOperation[SqlTransformer] with OperationDocumentation {

  override val id: Id = "6cba4400-d966-4a2a-8356-b37f37b4c73f"

  override val name: String = "SQL Transformation"

  override val description: String =
    "Executes an SQL transformation on a DataFrame"

  override lazy val tTagTO_1: TypeTag[SqlTransformer] = typeTag

  override val since: Version = Version(0, 4, 0)

}
