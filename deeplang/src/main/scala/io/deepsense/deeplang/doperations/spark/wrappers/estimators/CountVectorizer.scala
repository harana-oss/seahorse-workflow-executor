package io.deepsense.deeplang.doperations.spark.wrappers.estimators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.CountVectorizerEstimator
import io.deepsense.deeplang.doperables.spark.wrappers.models.CountVectorizerModel
import io.deepsense.deeplang.doperations.EstimatorAsOperation

class CountVectorizer extends EstimatorAsOperation[CountVectorizerEstimator, CountVectorizerModel]
    with SparkOperationDocumentation {

  override val id: Id = "e640d7df-d464-4ac0-99c4-235c29a0aa31"
  override val name: String = "Count Vectorizer"
  override val description: String =
    """Extracts the vocabulary from a given collection of documents and generates a vector
      |of token counts for each document.""".stripMargin

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#countvectorizer")
  override val since: Version = Version(1, 0, 0)
}
