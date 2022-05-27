package ai.deepsense.deeplang.actions.spark.wrappers.estimators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.RandomForestRegression
import ai.deepsense.deeplang.actions.EstimatorAsFactory

class CreateRandomForestRegression extends EstimatorAsFactory[RandomForestRegression] with SparkOperationDocumentation {

  override val id: Id = "2ec65504-bbe2-4ba2-a9b4-192e2f45ff16"

  override val name: String = "Random Forest Regression"

  override val description: String = "Random forest regression (RFR) is a learning " +
    "algorithm for regression. It supports both continuous and categorical features."

  override protected[this] val docsGuideLocation =
    Some("ml-classification-regression.html#random-forest-regression")

  override val since: Version = Version(1, 0, 0)

}
