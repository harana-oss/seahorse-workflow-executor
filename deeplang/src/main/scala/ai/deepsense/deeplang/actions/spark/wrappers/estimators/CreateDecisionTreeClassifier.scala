package ai.deepsense.deeplang.actions.spark.wrappers.estimators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.DecisionTreeClassifier
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.VanillaDecisionTreeClassifier
import ai.deepsense.deeplang.actions.EstimatorAsFactory

class CreateDecisionTreeClassifier extends EstimatorAsFactory[DecisionTreeClassifier] with SparkOperationDocumentation {

  override val id: Id = "81039036-bb26-445b-81b5-63fbc9295c00"

  override val name: String = "Decision Tree Classifier"

  override val description: String =
    """Creates a decision tree classifier.
      |It supports both binary and multiclass labels,
      |as well as both continuous and categorical features.""".stripMargin

  override protected[this] val docsGuideLocation =
    Some("ml-classification-regression.html#decision-tree-classifier")

  override val since: Version = Version(1, 1, 0)

}
