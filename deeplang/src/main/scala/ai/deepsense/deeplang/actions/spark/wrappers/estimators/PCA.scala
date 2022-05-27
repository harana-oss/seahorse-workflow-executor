package ai.deepsense.deeplang.actions.spark.wrappers.estimators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.PCAEstimator
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.PCAModel
import ai.deepsense.deeplang.actions.EstimatorAsOperation

class PCA extends EstimatorAsOperation[PCAEstimator, PCAModel] with SparkOperationDocumentation {

  override val id: Id = "fe1ac5fa-329a-4e3e-9cfc-67ee165053db"

  override val name: String = "PCA"

  override val description: String = "Trains a model to project vectors " +
    "to a low-dimensional space using PCA"

  override protected[this] val docsGuideLocation =
    Some("mllib-dimensionality-reduction.html#principal-component-analysis-pca")

  override val since: Version = Version(1, 0, 0)

}
