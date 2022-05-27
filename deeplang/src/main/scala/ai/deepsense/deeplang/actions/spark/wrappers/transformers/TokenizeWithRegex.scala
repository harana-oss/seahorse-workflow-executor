package ai.deepsense.deeplang.actions.spark.wrappers.transformers

import scala.reflect.runtime.universe.TypeTag

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action._
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers.RegexTokenizer
import ai.deepsense.deeplang.actions.TransformerAsOperation

class TokenizeWithRegex extends TransformerAsOperation[RegexTokenizer] with SparkOperationDocumentation {

  override val id: Id = "3fb50e0a-d4fb-474f-b6f3-679788068b1b"

  override val name: String = "Tokenize With Regex"

  override val description: String = "Splits text using a regular expression"

  override lazy val tTagTO_1: TypeTag[RegexTokenizer] = typeTag

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#tokenizer")

  override val since: Version = Version(1, 0, 0)

}
