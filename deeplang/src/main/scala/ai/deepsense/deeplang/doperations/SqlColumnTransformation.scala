package ai.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.DOperation.Id
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.doperables.SqlColumnTransformer

case class SqlColumnTransformation() extends TransformerAsOperation[SqlColumnTransformer] with OperationDocumentation {

  override val id: Id = "012876d9-7a72-47f9-98e4-8ed26db14d6d"

  override val name: String = "SQL Column Transformation"

  override val description: String =
    "Executes a SQL transformation on a column of a DataFrame"

  override lazy val tTagTO_1: TypeTag[SqlColumnTransformer] = typeTag

  override val since: Version = Version(1, 1, 0)

}
