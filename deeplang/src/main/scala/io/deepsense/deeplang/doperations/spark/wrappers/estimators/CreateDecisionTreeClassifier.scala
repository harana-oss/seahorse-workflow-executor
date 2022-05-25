package io.deepsense.deeplang.doperations.spark.wrappers.estimators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.{DecisionTreeClassifier, VanillaDecisionTreeClassifier}
import io.deepsense.deeplang.doperations.EstimatorAsFactory

class CreateDecisionTreeClassifier
  extends EstimatorAsFactory[DecisionTreeClassifier]
  with SparkOperationDocumentation {

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
