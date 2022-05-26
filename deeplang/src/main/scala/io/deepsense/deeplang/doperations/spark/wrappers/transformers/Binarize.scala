package io.deepsense.deeplang.doperations.spark.wrappers.transformers

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation._
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.transformers.Binarizer
import io.deepsense.deeplang.doperations.TransformerAsOperation

class Binarize extends TransformerAsOperation[Binarizer] with SparkOperationDocumentation {

  override val id: Id = "c29f2401-0891-4223-8a33-41ecbe316de6"

  override val name: String = "Binarize"

  override val description: String = "Binarizes continuous features"

  override lazy val tTagTO_1: TypeTag[Binarizer] = typeTag

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#binarizer")

  override val since: Version = Version(1, 0, 0)

}
