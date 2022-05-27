package ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers

import ai.deepsense.deeplang.parameters.selections.MultipleColumnSelection
import ai.deepsense.deeplang.parameters.selections.NameColumnSelection

class VectorAssemblerSmokeTest extends AbstractTransformerWrapperSmokeTest[VectorAssembler] {

  override def transformerWithParams: VectorAssembler = {
    val transformer = new VectorAssembler()
    transformer.set(
      Seq(
        transformer.inputColumns -> MultipleColumnSelection(Vector(NameColumnSelection(Set("i", "i2")))),
        transformer.outputColumn -> "outputVector"
      ): _*
    )
  }

}
