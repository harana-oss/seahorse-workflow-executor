package io.deepsense.deeplang.doperations.spark.wrappers.estimators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.PCAEstimator
import io.deepsense.deeplang.doperables.spark.wrappers.models.PCAModel
import io.deepsense.deeplang.doperations.EstimatorAsOperation

class PCA extends EstimatorAsOperation[PCAEstimator, PCAModel]
    with SparkOperationDocumentation {

  override val id: Id = "fe1ac5fa-329a-4e3e-9cfc-67ee165053db"
  override val name: String = "PCA"
  override val description: String = "Trains a model to project vectors " +
    "to a low-dimensional space using PCA"

  override protected[this] val docsGuideLocation =
    Some("mllib-dimensionality-reduction.html#principal-component-analysis-pca")
  override val since: Version = Version(1, 0, 0)
}
