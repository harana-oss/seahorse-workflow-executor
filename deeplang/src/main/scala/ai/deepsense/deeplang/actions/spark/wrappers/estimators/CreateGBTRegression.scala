package ai.deepsense.deeplang.actions.spark.wrappers.estimators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.GBTRegression
import ai.deepsense.deeplang.actions.EstimatorAsFactory

class CreateGBTRegression extends EstimatorAsFactory[GBTRegression] with SparkOperationDocumentation {

  override val id: Id = "e18c13f8-2108-46f0-979f-bba5a11ea312"

  override val name: String = "GBT Regression"

  override val description: String =
    """Gradient-Boosted Trees (GBTs) is a learning algorithm for regression. It supports both
      |continuous and categorical features.""".stripMargin

  override protected[this] val docsGuideLocation =
    Some("ml-classification-regression.html#gradient-boosted-tree-regression")

  override val since: Version = Version(1, 0, 0)

}
