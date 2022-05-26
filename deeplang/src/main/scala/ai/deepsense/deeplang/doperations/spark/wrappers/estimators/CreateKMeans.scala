package ai.deepsense.deeplang.doperations.spark.wrappers.estimators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.DOperation._
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.doperables.spark.wrappers.estimators.KMeans
import ai.deepsense.deeplang.doperations.EstimatorAsFactory

class CreateKMeans extends EstimatorAsFactory[KMeans] with SparkOperationDocumentation {

  override val id: Id = "2ecdd789-695d-4efa-98ad-63c80ae70f71"

  override val name: String = "K-Means"

  override val description: String =
    "Creates a k-means model. Note: Trained k-means model does not have any parameters."

  override protected[this] val docsGuideLocation =
    Some("ml-clustering.html#k-means")

  override val since: Version = Version(1, 0, 0)

}
