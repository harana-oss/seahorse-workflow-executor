package ai.deepsense.deeplang.doperables.spark.wrappers.transformers

import ai.deepsense.deeplang.params.selections.MultipleColumnSelection
import ai.deepsense.deeplang.params.selections.NameColumnSelection

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
