package ai.deepsense.deeplang.actionobjects

import scala.reflect.runtime.universe._

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.inference.exceptions.SparkTransformSchemaException
import ai.deepsense.deeplang.parameters.wrappers.spark.ParamsWithSparkWrappers
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.utils.TypeUtils

/** This class creates a Deeplang Transformer from a Spark ML Transformer. We assume that every Spark Transformer has a
  * no-arg constructor.
  *
  * @tparam T
  *   Wrapped Spark transformer type
  */
abstract class SparkTransformerWrapper[T <: ml.Transformer](implicit tag: TypeTag[T])
    extends Transformer
    with ParamsWithSparkWrappers {

  lazy val sparkTransformer: T = TypeUtils.instanceOfType(tag)

  override protected def applyTransform(ctx: ExecutionContext, df: DataFrame): DataFrame = {
    val paramMap = sparkParamMap(sparkTransformer, df.sparkDataFrame.schema)
    DataFrame.fromSparkDataFrame(sparkTransformer.transform(df.sparkDataFrame, paramMap))
  }

  override protected def applyTransformSchema(schema: StructType): Option[StructType] = {
    val paramMap                = sparkParamMap(sparkTransformer, schema)
    val transformerForInference = sparkTransformer.copy(paramMap)

    try
      Some(transformerForInference.transformSchema(schema))
    catch {
      case e: Exception => throw SparkTransformSchemaException(e)
    }
  }

}
