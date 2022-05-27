package ai.deepsense.deeplang.actions.spark.wrappers.estimators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action._
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.StringIndexerEstimator
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.StringIndexerModel
import ai.deepsense.deeplang.actions.EstimatorAsOperation
import ai.deepsense.deeplang.actions.MultiColumnEstimatorParamsForwarder

class StringIndexer
    extends EstimatorAsOperation[StringIndexerEstimator, StringIndexerModel]
    with MultiColumnEstimatorParamsForwarder[StringIndexerEstimator]
    with SparkOperationDocumentation {

  override val id: Id = "c9df7000-9ea0-41c0-b66c-3062fd57851b"

  override val name: String = "String Indexer"

  override val description: String =
    "Maps a string column of labels to an integer column of label indices"

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#stringindexer")

  override val since: Version = Version(1, 0, 0)

}
