package io.deepsense.deeplang.doperations.spark.wrappers.estimators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.IDFEstimator
import io.deepsense.deeplang.doperables.spark.wrappers.models.IDFModel
import io.deepsense.deeplang.doperations.EstimatorAsOperation

class IDF extends EstimatorAsOperation[IDFEstimator, IDFModel]
    with SparkOperationDocumentation {

  override val id: Id = "36d31a98-9238-4159-8298-64eb8e3ca55a"
  override val name: String = "IDF"
  override val description: String = "Computes the Inverse Document Frequency (IDF) " +
    "of a collection of documents"

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#tf-idf")
  override val since: Version = Version(1, 0, 0)
}
