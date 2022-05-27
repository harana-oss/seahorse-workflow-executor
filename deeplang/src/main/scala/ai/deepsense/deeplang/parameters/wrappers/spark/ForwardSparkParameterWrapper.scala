package ai.deepsense.deeplang.parameters.wrappers.spark

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

trait ForwardSparkParameterWrapper[P <: ml.param.Params, T] extends SparkParameterWrapper[P, T, T] {

  def convert(value: T)(schema: StructType): T = value

}
