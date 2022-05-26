package ai.deepsense.deeplang.doperables

import org.apache.spark.ml

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.doperables.serialization.CustomPersistence
import ai.deepsense.deeplang.doperables.serialization.SerializableSparkModel

/** This is a trait that lets define a method for loading model, and if it fails, it falls back to default load
  * implementation. It is especially useful for supporting two spark versions.
  */
trait LoadableWithFallback[M <: ml.Model[M], E <: ml.Estimator[M]] { self: SparkModelWrapper[M, E] =>

  def tryToLoadModel(path: String): Option[M]

  protected def loadModel(ctx: ExecutionContext, path: String): SerializableSparkModel[M] = {
    tryToLoadModel(path) match {
      case Some(m) => new SerializableSparkModel(m)
      case None    =>
        val modelPath = Transformer.modelFilePath(path)
        CustomPersistence.load[SerializableSparkModel[M]](ctx.sparkContext, modelPath)
    }
  }

}
