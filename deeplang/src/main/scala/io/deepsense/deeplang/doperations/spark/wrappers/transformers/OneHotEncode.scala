package io.deepsense.deeplang.doperations.spark.wrappers.transformers

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation._
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.transformers.OneHotEncoder
import io.deepsense.deeplang.doperations.TransformerAsOperation

class OneHotEncode extends TransformerAsOperation[OneHotEncoder] with SparkOperationDocumentation {

  override val id: Id = "33af92e5-57f2-4586-b176-961eb72ce5b0"

  override val name: String = "One Hot Encoder"

  override val description: String = "Maps a column of category indices to " +
    "a column of binary vectors"

  override lazy val tTagTO_1: TypeTag[OneHotEncoder] = typeTag

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#onehotencoder")

  override val since: Version = Version(1, 0, 0)

}
