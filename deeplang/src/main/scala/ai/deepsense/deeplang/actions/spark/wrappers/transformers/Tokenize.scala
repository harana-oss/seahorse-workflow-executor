package ai.deepsense.deeplang.actions.spark.wrappers.transformers

import scala.reflect.runtime.universe.TypeTag

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers.StringTokenizer
import ai.deepsense.deeplang.actions.TransformerAsOperation

class Tokenize extends TransformerAsOperation[StringTokenizer] with SparkOperationDocumentation {

  override val id: Id = "38751243-5e0e-435a-b366-8d225c9fd5ca"

  override val name: String = "Tokenize"

  override val description: String = "Converts text to lowercase and splits it by spaces"

  override lazy val tTagTO_1: TypeTag[StringTokenizer] = typeTag

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#tokenizer")

  override val since: Version = Version(1, 0, 0)

}
