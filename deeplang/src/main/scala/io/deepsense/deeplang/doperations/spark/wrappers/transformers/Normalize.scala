package io.deepsense.deeplang.doperations.spark.wrappers.transformers

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.transformers.Normalizer
import io.deepsense.deeplang.doperations.TransformerAsOperation

class Normalize extends TransformerAsOperation[Normalizer] with SparkOperationDocumentation {

  override val id: Id = "20f3d9ef-9b04-49c6-8acd-7ddafdedcb39"

  override val name: String = "Normalize"

  override val description: String = "Normalizes vector columns using given p-norm"

  override lazy val tTagTO_1: TypeTag[Normalizer] = typeTag

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#normalizer")

  override val since: Version = Version(1, 0, 0)

}
