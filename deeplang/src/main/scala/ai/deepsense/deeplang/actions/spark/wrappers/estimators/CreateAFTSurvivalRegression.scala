package ai.deepsense.deeplang.actions.spark.wrappers.estimators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.AFTSurvivalRegression
import ai.deepsense.deeplang.actions.EstimatorAsFactory

class CreateAFTSurvivalRegression extends EstimatorAsFactory[AFTSurvivalRegression] with SparkOperationDocumentation {

  override val id: Id = "e315aa7f-16f2-4fa5-8376-69a96171a57a"

  override val name: String = "AFT Survival Regression"

  override val description: String = "Creates an AFT survival regression model"

  override protected[this] val docsGuideLocation =
    Some("ml-classification-regression.html#survival-regression")

  override val since: Version = Version(1, 1, 0)

}
