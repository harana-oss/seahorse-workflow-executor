package io.deepsense.deeplang.doperations.spark.wrappers.transformers

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.transformers.StringTokenizer
import io.deepsense.deeplang.doperations.TransformerAsOperation

class Tokenize extends TransformerAsOperation[StringTokenizer]
    with SparkOperationDocumentation {

  override val id: Id = "38751243-5e0e-435a-b366-8d225c9fd5ca"
  override val name: String = "Tokenize"
  override val description: String = "Converts text to lowercase and splits it by spaces"

  override lazy val tTagTO_1: TypeTag[StringTokenizer] = typeTag

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#tokenizer")
  override val since: Version = Version(1, 0, 0)
}
