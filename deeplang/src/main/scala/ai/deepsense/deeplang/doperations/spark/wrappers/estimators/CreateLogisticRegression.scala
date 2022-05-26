package ai.deepsense.deeplang.doperations.spark.wrappers.estimators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.DOperation.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.doperables.spark.wrappers.estimators.LogisticRegression
import ai.deepsense.deeplang.doperations.EstimatorAsFactory

class CreateLogisticRegression extends EstimatorAsFactory[LogisticRegression] with SparkOperationDocumentation {

  override val id: Id = "7f9e459e-3e11-4c5f-9137-94447d53ff60"

  override val name: String = "Logistic Regression"

  override val description: String = "Creates a logistic regression model"

  override protected[this] val docsGuideLocation =
    Some("ml-classification-regression.html#logistic-regression")

  override val since: Version = Version(1, 0, 0)

}
