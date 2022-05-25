package io.deepsense.deeplang.doperations.spark.wrappers.estimators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.AFTSurvivalRegression
import io.deepsense.deeplang.doperations.EstimatorAsFactory

class CreateAFTSurvivalRegression extends EstimatorAsFactory[AFTSurvivalRegression]
    with SparkOperationDocumentation {

  override val id: Id = "e315aa7f-16f2-4fa5-8376-69a96171a57a"
  override val name: String = "AFT Survival Regression"
  override val description: String = "Creates an AFT survival regression model"

  override protected[this] val docsGuideLocation =
    Some("ml-classification-regression.html#survival-regression")
  override val since: Version = Version(1, 1, 0)
}
