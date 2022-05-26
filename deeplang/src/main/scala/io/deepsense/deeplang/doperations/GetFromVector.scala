package io.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.GetFromVectorTransformer

class GetFromVector extends TransformerAsOperation[GetFromVectorTransformer] with OperationDocumentation {

  override val id: Id = "241a23d1-97a0-41d0-bcf7-5c2ccb24e3d5"

  override val name: String = "Get From Vector"

  override val description: String =
    "Extracts single number from vector column"

  override lazy val tTagTO_1: TypeTag[GetFromVectorTransformer] = typeTag

  override val since: Version = Version(1, 2, 0)

}
