package ai.deepsense.deeplang.actions.spark.wrappers.estimators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.GBTClassifier
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.GBTClassificationModel
import ai.deepsense.deeplang.actions.EstimatorAsFactory

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
