package io.deepsense.deeplang.doperations.spark.wrappers.estimators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation._
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.StringIndexerEstimator
import io.deepsense.deeplang.doperables.spark.wrappers.models.StringIndexerModel
import io.deepsense.deeplang.doperations.{EstimatorAsOperation, MultiColumnEstimatorParamsForwarder}

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
