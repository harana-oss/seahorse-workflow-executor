package io.deepsense.deeplang.doperations

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation._
import io.deepsense.deeplang.doperables.RTransformer
import scala.reflect.runtime.universe.TypeTag

import io.deepsense.deeplang.documentation.OperationDocumentation

class RTransformation extends TransformerAsOperation[RTransformer] with OperationDocumentation {
  override val id: Id = "b578ad31-3a5b-4b94-a8d1-4c319fac6add"
  override val name: String = "R Transformation"
  override val description: String = "Creates a custom R transformation"

  override lazy val tTagTO_1: TypeTag[RTransformer] = typeTag

  override val since: Version = Version(1, 3, 0)
}

object RTransformation {
  val InputPortNumber: Int = 0
  val OutputPortNumber: Int = 0
}
