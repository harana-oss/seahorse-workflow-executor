
package io.deepsense.deeplang.doperables.serialization

import org.apache.spark.SparkContext

import io.deepsense.commons.serialization.Serialization
import io.deepsense.commons.utils.Logging

object CustomPersistence extends Logging {

  def save[T](sparkContext: SparkContext, instance: T, path: String): Unit = {
    val data: Array[Byte] = Serialization.serialize(instance)
    val rdd = sparkContext.parallelize(data, 1)
    rdd.saveAsTextFile(path)
  }

  def load[T](sparkContext: SparkContext, path: String): T = {
    logger.debug("Reading objects from: {}", path)
    val rdd = sparkContext.textFile(path)
    val data: Array[Byte] = rdd.map(_.toByte).collect()
    Serialization.deserialize(data)
  }
}
