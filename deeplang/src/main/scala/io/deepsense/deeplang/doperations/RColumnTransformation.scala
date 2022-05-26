package io.deepsense.deeplang.doperations

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation._
import io.deepsense.deeplang.doperables.RColumnTransformer
import scala.reflect.runtime.universe.TypeTag

import io.deepsense.deeplang.documentation.OperationDocumentation

class RColumnTransformation extends TransformerAsOperation[RColumnTransformer] with OperationDocumentation {

  override val id: Id = "52e2652a-0c90-445e-87e9-a04f92ff75f0"

  override val name: String = "R Column Transformation"

  override val description: String =
    "Executes a custom R transformation on a column of a DataFrame"

  override lazy val tTagTO_1: TypeTag[RColumnTransformer] = typeTag

  override val since: Version = Version(1, 3, 0)

}
