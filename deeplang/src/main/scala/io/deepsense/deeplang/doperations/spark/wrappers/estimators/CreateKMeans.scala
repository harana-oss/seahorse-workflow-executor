package io.deepsense.deeplang.doperations.spark.wrappers.estimators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation._
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.KMeans
import io.deepsense.deeplang.doperations.EstimatorAsFactory

class CreateKMeans extends EstimatorAsFactory[KMeans] with SparkOperationDocumentation {

  override val id: Id = "2ecdd789-695d-4efa-98ad-63c80ae70f71"

  override val name: String = "K-Means"

  override val description: String =
    "Creates a k-means model. Note: Trained k-means model does not have any parameters."

  override protected[this] val docsGuideLocation =
    Some("ml-clustering.html#k-means")

  override val since: Version = Version(1, 0, 0)

}
