package io.deepsense.deeplang.doperations.spark.wrappers.transformers

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation._
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.transformers.PolynomialExpander
import io.deepsense.deeplang.doperations.TransformerAsOperation

class PolynomialExpand extends TransformerAsOperation[PolynomialExpander] with SparkOperationDocumentation {

  override val id: Id = "4a741088-3180-4373-940d-741b2f1620de"

  override val name: String = "Polynomial Expansion"

  override val description: String = "Applies polynomial expansion to vector columns"

  override lazy val tTagTO_1: TypeTag[PolynomialExpander] = typeTag

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#polynomialexpansion")

  override val since: Version = Version(1, 0, 0)

}
