package ai.deepsense.deeplang.doperations.spark.wrappers.transformers

import scala.reflect.runtime.universe.TypeTag

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.DOperation._
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.doperables.spark.wrappers.transformers.StopWordsRemover
import ai.deepsense.deeplang.doperations.TransformerAsOperation

class RemoveStopWords extends TransformerAsOperation[StopWordsRemover] with SparkOperationDocumentation {

  override val id: Id = "39acf60c-3f57-4346-ada7-6959a76568a5"

  override val name: String = "Remove Stop Words"

  override val description: String =
    """Filters out default English stop words from the input.
      |Null values from the input array are preserved.""".stripMargin

  override lazy val tTagTO_1: TypeTag[StopWordsRemover] = typeTag

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#stopwordsremover")

  override val since: Version = Version(1, 0, 0)

}
