package ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers

import java.nio.file.Files
import java.nio.file.Path

import org.apache.commons.io.FileUtils
import org.scalatest.BeforeAndAfter
import org.scalatest.Suite

import ai.deepsense.deeplang.actionobjects.Transformer
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.DeeplangIntegTestSupport
import ai.deepsense.deeplang.ExecutionContext

trait TransformerSerialization extends Suite with BeforeAndAfter {

  var tempDir: Path = _

  before {
    tempDir = Files.createTempDirectory("writeReadTransformer")
  }

  after {
    FileUtils.deleteDirectory(tempDir.toFile)
  }

}

object TransformerSerialization {

  implicit class TransformerSerializationOps(private val transformer: Transformer) {

    def applyTransformationAndSerialization(path: Path, df: DataFrame)(implicit
        executionContext: ExecutionContext
    ): DataFrame = {
      val result                          = transformer._transform(executionContext, df)
      val deserialized                    = loadSerializedTransformer(path)
      val resultFromSerializedTransformer = deserialized._transform(executionContext, df)
      DeeplangIntegTestSupport.assertDataFramesEqual(result, resultFromSerializedTransformer)
      result
    }

    def loadSerializedTransformer(path: Path)(implicit executionContext: ExecutionContext): Transformer = {
      val outputPath: Path = path.resolve(this.getClass.getName)
      transformer.save(executionContext, outputPath.toString)
      Transformer.load(executionContext, outputPath.toString)
    }

  }

}
