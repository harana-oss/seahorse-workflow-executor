package ai.deepsense.deeplang.params.wrappers.spark

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

trait ForwardSparkParamWrapper[P <: ml.param.Params, T] extends SparkParamWrapper[P, T, T] {

  def convert(value: T)(schema: StructType): T = value

}
