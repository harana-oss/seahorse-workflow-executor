package io.deepsense.deeplang.doperations.spark.wrappers.estimators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.MinMaxScalerEstimator
import io.deepsense.deeplang.doperables.spark.wrappers.models.MinMaxScalerModel
import io.deepsense.deeplang.doperations.EstimatorAsOperation

class MinMaxScaler extends EstimatorAsOperation[MinMaxScalerEstimator, MinMaxScalerModel]
    with SparkOperationDocumentation {

  override val id: Id = "a63b6de3-793b-4cbd-ae81-76de216d90d5"
  override val name: String = "Min-Max Scaler"
  override val description: String =
    """Linearly rescales each feature to a common range [min, max] using column summary statistics.
      |The operation is also known as Min-Max normalization or rescaling.""".stripMargin

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#minmaxscaler")
  override val since: Version = Version(1, 0, 0)
}
