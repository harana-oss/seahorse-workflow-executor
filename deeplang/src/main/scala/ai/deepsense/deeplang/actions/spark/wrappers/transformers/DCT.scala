package ai.deepsense.deeplang.actions.spark.wrappers.transformers

import scala.reflect.runtime.universe.TypeTag

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers.DiscreteCosineTransformer
import ai.deepsense.deeplang.actions.TransformerAsOperation

class DCT extends TransformerAsOperation[DiscreteCosineTransformer] with SparkOperationDocumentation {

  override val id: Id = "68cd1492-501d-4c4f-9fde-f742d652111a"

  override val name: String = "DCT"

  override val description: String = "Applies discrete cosine transform (DCT) to vector columns"

  override lazy val tTagTO_1: TypeTag[DiscreteCosineTransformer] = typeTag

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#discrete-cosine-transform-dct")

  override val since: Version = Version(1, 0, 0)

}
