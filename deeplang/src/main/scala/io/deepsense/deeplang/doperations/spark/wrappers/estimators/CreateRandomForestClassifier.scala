package io.deepsense.deeplang.doperations.spark.wrappers.estimators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.RandomForestClassifier
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.LogisticRegression
import io.deepsense.deeplang.doperations.EstimatorAsFactory

class CreateRandomForestClassifier extends EstimatorAsFactory[RandomForestClassifier] with SparkOperationDocumentation {

  override val id: Id = "7cd334e2-bd40-42db-bea1-7592f12302f2"

  override val name: String = "Random Forest Classifier"

  override val description: String = "Creates a random forest classification model"

  override protected[this] val docsGuideLocation =
    Some("ml-classification-regression.html#random-forest-classifier")

  override val since: Version = Version(1, 1, 0)

}
