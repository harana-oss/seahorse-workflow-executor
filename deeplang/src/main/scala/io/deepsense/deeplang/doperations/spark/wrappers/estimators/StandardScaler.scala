package io.deepsense.deeplang.doperations.spark.wrappers.estimators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.StandardScalerEstimator
import io.deepsense.deeplang.doperables.spark.wrappers.models.StandardScalerModel
import io.deepsense.deeplang.doperations.EstimatorAsOperation

class StandardScaler extends EstimatorAsOperation[StandardScalerEstimator, StandardScalerModel]
    with SparkOperationDocumentation {

  override val id: Id = "85007b46-210c-4e88-b7dc-cf46d3803b06"
  override val name: String = "Standard Scaler"
  override val description: String = "Standardizes features by removing the mean and scaling " +
    "to unit variance using column summary statistics on the samples in the training set"

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#standardscaler")
  override val since: Version = Version(1, 0, 0)
}
