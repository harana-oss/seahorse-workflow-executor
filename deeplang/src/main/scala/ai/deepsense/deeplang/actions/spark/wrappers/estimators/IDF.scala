package ai.deepsense.deeplang.actions.spark.wrappers.estimators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.IDFEstimator
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.IDFModel
import ai.deepsense.deeplang.actions.EstimatorAsOperation

class IDF extends EstimatorAsOperation[IDFEstimator, IDFModel] with SparkOperationDocumentation {

  override val id: Id = "36d31a98-9238-4159-8298-64eb8e3ca55a"

  override val name: String = "IDF"

  override val description: String = "Computes the Inverse Document Frequency (IDF) " +
    "of a collection of documents"

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#tf-idf")

  override val since: Version = Version(1, 0, 0)

}
