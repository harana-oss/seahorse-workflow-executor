package io.deepsense.deeplang.doperables.spark.wrappers.transformers

import java.nio.file.Files
import java.nio.file.Path

import org.apache.commons.io.FileUtils
import org.scalatest.BeforeAndAfter
import org.scalatest.Suite

import io.deepsense.deeplang.doperables.Transformer
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.DeeplangIntegTestSupport
import io.deepsense.deeplang.ExecutionContext

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
