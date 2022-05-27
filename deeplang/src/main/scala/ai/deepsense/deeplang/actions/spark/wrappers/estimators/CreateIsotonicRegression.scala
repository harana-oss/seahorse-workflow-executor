package ai.deepsense.deeplang.actions.spark.wrappers.estimators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.IsotonicRegression
import ai.deepsense.deeplang.actions.EstimatorAsFactory

class CreateIsotonicRegression extends EstimatorAsFactory[IsotonicRegression] with SparkOperationDocumentation {

  override val id: Id = "0aebeb36-058c-49ef-a1be-7974ef56b564"

  override val name: String = "Isotonic Regression"

  override val description: String = "Creates an isotonic regression model"

  override protected[this] val docsGuideLocation =
    Some("mllib-isotonic-regression.html")

  override val since: Version = Version(1, 0, 0)

}
