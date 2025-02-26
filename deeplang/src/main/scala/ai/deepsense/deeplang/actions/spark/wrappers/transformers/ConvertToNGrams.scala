package ai.deepsense.deeplang.actions.spark.wrappers.transformers

import scala.reflect.runtime.universe.TypeTag

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers.NGramTransformer
import ai.deepsense.deeplang.actions.TransformerAsOperation

class ConvertToNGrams extends TransformerAsOperation[NGramTransformer] with SparkOperationDocumentation {

  override val id: Id = "06a73bfe-4e1a-4cde-ae6c-ad5a31f72496"

  override val name: String = "Convert To n-grams"

  override val description: String = "Converts arrays of strings to arrays of n-grams. Null " +
    "values in the input arrays are ignored. Each n-gram is represented by a space-separated " +
    "string of words. When the input is empty, an empty array is returned. When the input array " +
    "is shorter than n (number of elements per n-gram), no n-grams are returned."

  override lazy val tTagTO_1: TypeTag[NGramTransformer] = typeTag

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#n-gram")

  override val since: Version = Version(1, 0, 0)

}
