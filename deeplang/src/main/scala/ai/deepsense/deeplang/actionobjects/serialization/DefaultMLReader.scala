package ai.deepsense.deeplang.actionobjects.serialization

import org.apache.spark.ml.util.MLReader

import ai.deepsense.deeplang.actionobjects.Transformer
import ai.deepsense.sparkutils.ML.MLReaderWithSparkContext

class DefaultMLReader[T] extends MLReader[T] with MLReaderWithSparkContext {

  override def load(path: String): T = {
    val modelPath = Transformer.modelFilePath(path)
    CustomPersistence.load(sparkContext, modelPath)
  }

}
