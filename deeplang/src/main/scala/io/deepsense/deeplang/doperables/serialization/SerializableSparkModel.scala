package io.deepsense.deeplang.doperables.serialization

import org.apache.spark.ml.Model
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.util.MLReadable
import org.apache.spark.ml.util.MLReader
import org.apache.spark.ml.util.MLWritable
import org.apache.spark.ml.util.MLWriter
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.StructType

import io.deepsense.sparkutils.ML

class SerializableSparkModel[M <: Model[M]](val sparkModel: M)
    extends ML.Model[SerializableSparkModel[M]]
    with MLWritable {

  override def copy(extra: ParamMap): SerializableSparkModel[M] =
    new SerializableSparkModel(sparkModel.copy(extra))

  override def write: MLWriter =
    sparkModel match {
      case w: MLWritable => w.write
      case _             => new DefaultMLWriter(this)
    }

  override def transformDF(dataset: DataFrame): DataFrame = sparkModel.transform(dataset)

  override def transformSchema(schema: StructType): StructType = sparkModel.transformSchema(schema)

  override val uid: String = "dc7178fe-b209-44f5-8a74-d3c4dafa0fae"

}

// This class may seem unused, but it is used reflectively by spark deserialization mechanism
object SerializableSparkModel extends MLReadable[SerializableSparkModel[_]] {

  override def read: MLReader[SerializableSparkModel[_]] =
    new DefaultMLReader[SerializableSparkModel[_]]()

}
