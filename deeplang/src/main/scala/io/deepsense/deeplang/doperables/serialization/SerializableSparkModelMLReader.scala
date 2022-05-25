
package io.deepsense.deeplang.doperables.serialization

import org.apache.spark.ml.util.MLReader

import io.deepsense.deeplang.doperables.Transformer
import io.deepsense.sparkutils.ML
import io.deepsense.sparkutils.ML.MLReaderWithSparkContext

class SerializableSparkModelMLReader[M <: ML.Model[M]]
    extends MLReader[SerializableSparkModel[M]]
    with MLReaderWithSparkContext {

  override def load(path: String): SerializableSparkModel[M] = {
    val modelPath = Transformer.modelFilePath(path)
    new SerializableSparkModel(CustomPersistence.load[M](sparkContext, modelPath))
  }
}
