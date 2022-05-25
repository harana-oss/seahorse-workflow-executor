package io.deepsense.deeplang.doperations.spark.wrappers.estimators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.LinearRegression
import io.deepsense.deeplang.doperations.EstimatorAsFactory

class CreateLinearRegression extends EstimatorAsFactory[LinearRegression]
    with SparkOperationDocumentation {

  override val id: Id = "461a7b68-5fc8-4cd7-a912-0e0cc70eb3aa"
  override val name: String = "Linear Regression"
  override val description: String = "Creates a linear regression model"

  override protected[this] val docsGuideLocation =
    Some("ml-classification-regression.html#linear-regression")
  override val since: Version = Version(1, 0, 0)
}
