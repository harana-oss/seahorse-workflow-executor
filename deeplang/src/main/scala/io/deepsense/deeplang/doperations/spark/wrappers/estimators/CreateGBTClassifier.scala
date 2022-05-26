package io.deepsense.deeplang.doperations.spark.wrappers.estimators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.GBTClassifier
import io.deepsense.deeplang.doperables.spark.wrappers.models.GBTClassificationModel
import io.deepsense.deeplang.doperations.EstimatorAsFactory

class CreateGBTClassifier extends EstimatorAsFactory[GBTClassifier] with SparkOperationDocumentation {

  override val id: Id = "98275271-9817-4add-85d7-e6eade3e5b81"

  override val name: String = "GBT Classifier"

  override val description: String =
    "Gradient-Boosted Trees (GBTs) is a learning algorithm for classification." +
      " It supports binary labels, as well as both continuous and categorical features." +
      " Note: Multiclass labels are not currently supported."

  override protected[this] val docsGuideLocation =
    Some("ml-classification-regression.html#gradient-boosted-tree-classifier")

  override val since: Version = Version(1, 0, 0)

}
