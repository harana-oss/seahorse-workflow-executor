package ai.deepsense.deeplang.doperations.spark.wrappers.transformers

import scala.reflect.runtime.universe.TypeTag

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.DOperation.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.doperables.spark.wrappers.transformers.VectorAssembler
import ai.deepsense.deeplang.doperations.TransformerAsOperation

class AssembleVector extends TransformerAsOperation[VectorAssembler] with SparkOperationDocumentation {

  override val id: Id = "c57a5b99-9184-4095-9037-9359f905628d"

  override val name: String = "Assemble Vector"

  override val description: String = "Merges multiple columns into a single vector column"

  override lazy val tTagTO_1: TypeTag[VectorAssembler] = typeTag

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#vectorassembler")

  override val since: Version = Version(1, 0, 0)

}
