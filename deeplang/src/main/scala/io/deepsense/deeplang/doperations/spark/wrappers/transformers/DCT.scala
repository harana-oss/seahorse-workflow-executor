package io.deepsense.deeplang.doperations.spark.wrappers.transformers

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.transformers.DiscreteCosineTransformer
import io.deepsense.deeplang.doperations.TransformerAsOperation

class DCT extends TransformerAsOperation[DiscreteCosineTransformer]
    with SparkOperationDocumentation {

  override val id: Id = "68cd1492-501d-4c4f-9fde-f742d652111a"
  override val name: String = "DCT"
  override val description: String = "Applies discrete cosine transform (DCT) to vector columns"

  override lazy val tTagTO_1: TypeTag[DiscreteCosineTransformer] = typeTag

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#discrete-cosine-transform-dct")
  override val since: Version = Version(1, 0, 0)
}
