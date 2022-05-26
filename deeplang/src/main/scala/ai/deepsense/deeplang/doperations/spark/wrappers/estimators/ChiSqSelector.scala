package ai.deepsense.deeplang.doperations.spark.wrappers.estimators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.DOperation._
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.doperables.spark.wrappers.estimators.ChiSqSelectorEstimator
import ai.deepsense.deeplang.doperables.spark.wrappers.models.ChiSqSelectorModel
import ai.deepsense.deeplang.doperations.EstimatorAsOperation

class ChiSqSelector
    extends EstimatorAsOperation[ChiSqSelectorEstimator, ChiSqSelectorModel]
    with SparkOperationDocumentation {

  override val id: Id = "7355518a-4581-4048-b8b2-880cdb212205"

  override val name: String = "Chi-Squared Selector"

  override val description: String =
    "Selects categorical features to use for predicting a categorical label."

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#chisqselector")

  override val since: Version = Version(1, 1, 0)

}
