package ai.deepsense.deeplang.doperations

import ai.deepsense.deeplang.DeeplangIntegTestSupport
import ai.deepsense.deeplang.doperables.Transformer

abstract class WriteReadTransformerIntegTest extends DeeplangIntegTestSupport {

  def writeReadTransformer(transformer: Transformer, outputFile: String): Unit = {

    val writeTransformer: WriteTransformer = WriteTransformer(outputFile).setShouldOverwrite(true)
    val readTransformer: ReadTransformer   = ReadTransformer(outputFile)

    writeTransformer.executeUntyped(Vector(transformer))(executionContext)
    val deserializedTransformer =
      readTransformer.executeUntyped(Vector())(executionContext).head.asInstanceOf[Transformer]

    deserializedTransformer shouldBe transformer
  }

  def writeTransformer(transformer: Transformer, outputFile: String, overwrite: Boolean): Unit = {
    val writeTransformer: WriteTransformer = WriteTransformer(outputFile).setShouldOverwrite(overwrite)
    writeTransformer.executeUntyped(Vector(transformer))(executionContext)
  }

}
