package io.deepsense.deeplang.doperations.spark.wrappers.estimators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation._
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.ChiSqSelectorEstimator
import io.deepsense.deeplang.doperables.spark.wrappers.models.ChiSqSelectorModel
import io.deepsense.deeplang.doperations.EstimatorAsOperation

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
