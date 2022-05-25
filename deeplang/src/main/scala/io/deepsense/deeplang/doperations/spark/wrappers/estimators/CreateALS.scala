package io.deepsense.deeplang.doperations.spark.wrappers.estimators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.ALS
import io.deepsense.deeplang.doperations.EstimatorAsFactory

class CreateALS extends EstimatorAsFactory[ALS]
    with SparkOperationDocumentation {

  override val id: Id = "5a9e4883-b653-418e-bc51-a42fde476a63"
  override val name: String = "ALS"
  override val description: String = "Creates an ALS recommendation model"

  override protected[this] val docsGuideLocation =
    Some("mllib-collaborative-filtering.html#collaborative-filtering")
  override val since: Version = Version(1, 0, 0)
}
