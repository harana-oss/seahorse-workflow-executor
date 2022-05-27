package ai.deepsense.deeplang.actions.spark.wrappers.estimators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.VectorIndexerEstimator
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.VectorIndexerModel
import ai.deepsense.deeplang.actions.EstimatorAsOperation

class VectorIndexer
    extends EstimatorAsOperation[VectorIndexerEstimator, VectorIndexerModel]
    with SparkOperationDocumentation {

  override val id: Id = "d62abcbf-1540-4d58-8396-a92b017f2ef0"

  override val name: String = "Vector Indexer"

  override val description: String =
    """Vector Indexer indexes categorical features inside of a Vector. It decides which features
      |are categorical and converts them to category indices. The decision is based on the number of
      |distinct values of a feature.""".stripMargin

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#vectorindexer")

  override val since: Version = Version(1, 0, 0)

}
