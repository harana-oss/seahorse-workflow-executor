package io.deepsense.deeplang.doperations.spark.wrappers.estimators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.RandomForestRegression
import io.deepsense.deeplang.doperations.EstimatorAsFactory

class CreateRandomForestRegression extends EstimatorAsFactory[RandomForestRegression]
    with SparkOperationDocumentation {

  override val id: Id = "2ec65504-bbe2-4ba2-a9b4-192e2f45ff16"
  override val name: String = "Random Forest Regression"
  override val description: String = "Random forest regression (RFR) is a learning " +
    "algorithm for regression. It supports both continuous and categorical features."

  override protected[this] val docsGuideLocation =
    Some("ml-classification-regression.html#random-forest-regression")
  override val since: Version = Version(1, 0, 0)
}
