package io.deepsense.deeplang.doperables.serialization

import org.apache.spark.ml.util.MLReader

import io.deepsense.deeplang.doperables.Transformer
import io.deepsense.sparkutils.ML.MLReaderWithSparkContext

class DefaultMLReader[T] extends MLReader[T] with MLReaderWithSparkContext {

  override def load(path: String): T = {
    val modelPath = Transformer.modelFilePath(path)
    CustomPersistence.load(sparkContext, modelPath)
  }

}
