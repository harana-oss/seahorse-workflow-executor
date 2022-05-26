package io.deepsense.deeplang.doperations.spark.wrappers.estimators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.GBTRegression
import io.deepsense.deeplang.doperations.EstimatorAsFactory

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
