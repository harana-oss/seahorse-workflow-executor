package ai.deepsense.deeplang.actions.spark.wrappers.transformers

import scala.reflect.runtime.universe.TypeTag

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action._
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers.PolynomialExpander
import ai.deepsense.deeplang.actions.TransformerAsOperation

class PolynomialExpand extends TransformerAsOperation[PolynomialExpander] with SparkOperationDocumentation {

  override val id: Id = "4a741088-3180-4373-940d-741b2f1620de"

  override val name: String = "Polynomial Expansion"

  override val description: String = "Applies polynomial expansion to vector columns"

  override lazy val tTagTO_1: TypeTag[PolynomialExpander] = typeTag

  override protected[this] val docsGuideLocation =
    Some("ml-features.html#polynomialexpansion")

  override val since: Version = Version(1, 0, 0)

}
