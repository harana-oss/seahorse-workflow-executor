package io.deepsense.deeplang.doperations.spark.wrappers.estimators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.DecisionTreeRegression
import io.deepsense.deeplang.doperations.EstimatorAsFactory

class CreateDecisionTreeRegression
  extends EstimatorAsFactory[DecisionTreeRegression]
  with SparkOperationDocumentation {

  override val id: Id = "a88db4fb-695e-4f44-8435-4999ccde36de"
  override val name: String = "Decision Tree Regression"
  override val description: String =
    """Creates a decision tree regression model.
      |It supports both continuous and categorical features.""".stripMargin

  override protected[this] val docsGuideLocation =
    Some("ml-classification-regression.html#decision-tree-regression")
  override val since: Version = Version(1, 1, 0)
}
