package io.deepsense.deeplang.doperations.spark.wrappers.transformers

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.transformers.HashingTFTransformer
import io.deepsense.deeplang.doperations.TransformerAsOperation

class HashingTF extends TransformerAsOperation[HashingTFTransformer]
    with SparkOperationDocumentation {

  override val id: Id = "4266c9c0-6863-44ca-967b-62927ca34434"
  override val name: String = "HashingTF"
  override val description: String =
    "Maps a sequence of terms to term frequencies using the hashing trick"

  override lazy val tTagTO_1: TypeTag[HashingTFTransformer] = typeTag

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#tf-idf")
  override val since: Version = Version(1, 0, 0)
}
