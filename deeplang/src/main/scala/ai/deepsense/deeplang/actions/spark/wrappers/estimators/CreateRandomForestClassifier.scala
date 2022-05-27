package ai.deepsense.deeplang.actions.spark.wrappers.estimators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.RandomForestClassifier
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.LogisticRegression
import ai.deepsense.deeplang.actions.EstimatorAsFactory

class CreateRandomForestClassifier extends EstimatorAsFactory[RandomForestClassifier] with SparkOperationDocumentation {

  override val id: Id = "7cd334e2-bd40-42db-bea1-7592f12302f2"

  override val name: String = "Random Forest Classifier"

  override val description: String = "Creates a random forest classification model"

  override protected[this] val docsGuideLocation =
    Some("ml-classification-regression.html#random-forest-classifier")

  override val since: Version = Version(1, 1, 0)

}
