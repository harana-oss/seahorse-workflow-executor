package io.deepsense.deeplang.doperations.spark.wrappers.transformers

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation._
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.transformers.RegexTokenizer
import io.deepsense.deeplang.doperations.TransformerAsOperation

class TokenizeWithRegex extends TransformerAsOperation[RegexTokenizer] with SparkOperationDocumentation {

  override val id: Id = "3fb50e0a-d4fb-474f-b6f3-679788068b1b"

  override val name: String = "Tokenize With Regex"

  override val description: String = "Splits text using a regular expression"

  override lazy val tTagTO_1: TypeTag[RegexTokenizer] = typeTag

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#tokenizer")

  override val since: Version = Version(1, 0, 0)

}
