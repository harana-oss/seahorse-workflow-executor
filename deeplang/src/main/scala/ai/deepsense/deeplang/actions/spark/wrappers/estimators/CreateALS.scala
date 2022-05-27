package ai.deepsense.deeplang.actions.spark.wrappers.estimators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.ALS
import ai.deepsense.deeplang.actions.EstimatorAsFactory

class CreateALS extends EstimatorAsFactory[ALS] with SparkOperationDocumentation {

  override val id: Id = "5a9e4883-b653-418e-bc51-a42fde476a63"

  override val name: String = "ALS"

  override val description: String = "Creates an ALS recommendation model"

  override protected[this] val docsGuideLocation =
    Some("mllib-collaborative-filtering.html#collaborative-filtering")

  override val since: Version = Version(1, 0, 0)

}
