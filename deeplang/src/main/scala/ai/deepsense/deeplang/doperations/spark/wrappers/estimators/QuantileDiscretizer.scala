package ai.deepsense.deeplang.doperations.spark.wrappers.estimators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.DOperation._
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.doperables.spark.wrappers.estimators.QuantileDiscretizerEstimator
import ai.deepsense.deeplang.doperables.spark.wrappers.models.QuantileDiscretizerModel
import ai.deepsense.deeplang.doperations.EstimatorAsOperation

class QuantileDiscretizer
    extends EstimatorAsOperation[QuantileDiscretizerEstimator, QuantileDiscretizerModel]
    with SparkOperationDocumentation {

  override val id: Id = "986e0b10-09de-44e9-a5b1-1dcc5fb53bd1"

  override val name: String = "Quantile Discretizer"

  override val description: String =
    "Takes a column with continuous features and outputs a column with binned categorical features."

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#quantilediscretizer")

  override val since: Version = Version(1, 1, 0)

}
