package ai.deepsense.deeplang.actionobjects.serialization

import org.apache.spark.ml.util.MLReader

import ai.deepsense.deeplang.actionobjects.Transformer
import ai.deepsense.sparkutils.ML
import ai.deepsense.sparkutils.ML.MLReaderWithSparkContext

class SerializableSparkModelMLReader[M <: ML.Model[M]]
    extends MLReader[SerializableSparkModel[M]]
    with MLReaderWithSparkContext {

  override def load(path: String): SerializableSparkModel[M] = {
    val modelPath = Transformer.modelFilePath(path)
    new SerializableSparkModel(CustomPersistence.load[M](sparkContext, modelPath))
  }

}
