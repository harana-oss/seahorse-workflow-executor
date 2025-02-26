package ai.deepsense.deeplang.actionobjects.serialization

import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.util.MLWritable
import org.apache.spark.ml.util.MLWriter
import org.apache.spark.ml.Estimator
import org.apache.spark.ml.Model
import org.apache.spark.sql
import org.apache.spark.sql.types.StructType

import ai.deepsense.sparkutils.ML

class SerializableSparkEstimator[T <: Model[T], E <: Estimator[T]](val sparkEstimator: E)
    extends ML.Estimator[SerializableSparkModel[T]]
    with MLWritable {

  override val uid: String = "e2a121fe-da6e-4ef2-9c5e-56ee558c14f0"

  override def fitDF(dataset: sql.DataFrame): SerializableSparkModel[T] = {
    val result: T = sparkEstimator.fit(dataset)
    new SerializableSparkModel[T](result)
  }

  override def copy(extra: ParamMap): Estimator[SerializableSparkModel[T]] =
    new SerializableSparkEstimator[T, E](sparkEstimator.copy(extra).asInstanceOf[E])

  override def write: MLWriter = new DefaultMLWriter(this)

  override def transformSchema(schema: StructType): StructType =
    sparkEstimator.transformSchema(schema)

}
