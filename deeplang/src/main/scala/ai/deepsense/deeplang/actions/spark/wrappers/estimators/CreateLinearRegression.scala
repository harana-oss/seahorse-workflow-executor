package ai.deepsense.deeplang.actions.spark.wrappers.estimators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.LinearRegression
import ai.deepsense.deeplang.actions.EstimatorAsFactory

class CreateLinearRegression extends EstimatorAsFactory[LinearRegression] with SparkOperationDocumentation {

  override val id: Id = "461a7b68-5fc8-4cd7-a912-0e0cc70eb3aa"

  override val name: String = "Linear Regression"

  override val description: String = "Creates a linear regression model"

  override protected[this] val docsGuideLocation =
    Some("ml-classification-regression.html#linear-regression")

  override val since: Version = Version(1, 0, 0)

}
