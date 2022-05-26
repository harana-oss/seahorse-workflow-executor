package ai.deepsense.deeplang.doperations.spark.wrappers.estimators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.DOperation.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.doperables.spark.wrappers.estimators.Word2VecEstimator
import ai.deepsense.deeplang.doperables.spark.wrappers.models.Word2VecModel
import ai.deepsense.deeplang.doperations.EstimatorAsOperation

class Word2Vec extends EstimatorAsOperation[Word2VecEstimator, Word2VecModel] with SparkOperationDocumentation {

  override val id: Id = "131c6765-6b60-44c7-9a09-0f79fbb4ad2f"

  override val name: String = "Word2Vec"

  override val description: String =
    """Transforms vectors of words into vectors of numeric codes for the purpose of further
      |processing by NLP or machine learning algorithms.""".stripMargin

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#word2vec")

  override val since: Version = Version(1, 0, 0)

}
