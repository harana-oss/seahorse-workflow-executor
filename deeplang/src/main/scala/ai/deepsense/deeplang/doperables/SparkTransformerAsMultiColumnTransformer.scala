package ai.deepsense.deeplang.doperables

import scala.language.reflectiveCalls
import scala.reflect.runtime.universe._

import org.apache.spark.ml.{Transformer => SparkTransformer}
import org.apache.spark.sql.types._

import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import ai.deepsense.deeplang.inference.exceptions.SparkTransformSchemaException
import ai.deepsense.deeplang.params.Param
import ai.deepsense.deeplang.params.wrappers.spark.ParamsWithSparkWrappers
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.TypeUtils

/** This class creates a Deeplang MultiColumnTransformer from a Spark ML Transformer that has inputCol and outputCol
  * parameters. We assume that every Spark Transformer has a no-arg constructor.
  *
  * @tparam T
  *   Wrapped Spark Transformer type
  */
abstract class SparkTransformerAsMultiColumnTransformer[
    T <: SparkTransformer {
      def setInputCol(value: String): T
      def setOutputCol(value: String): T
    }
](implicit tag: TypeTag[T])
    extends MultiColumnTransformer
    with ParamsWithSparkWrappers {

  lazy val sparkTransformer: T = TypeUtils.instanceOfType(tag)

  /** Determines whether to perform automatic conversion of numeric input column to one-element vector column. Can be
    * overridden in `Transformer` implementation.
    */
  def convertInputNumericToVector: Boolean = false

  def convertOutputVectorToDouble: Boolean = false

  override protected def getSpecificParams: Array[Param[_]] = Array()

  override def transformSingleColumn(
      inputColumn: String,
      outputColumn: String,
      context: ExecutionContext,
      dataFrame: DataFrame
  ): DataFrame = {
    val transformer = sparkTransformerWithParams(dataFrame.sparkDataFrame.schema)
    transformer.setInputCol(inputColumn)
    transformer.setOutputCol(outputColumn)
    if (
      convertInputNumericToVector
      && NumericToVectorUtils.isColumnNumeric(dataFrame.schema.get, inputColumn)
    ) {
      // Automatically convert numeric input column to one-element vector column
      val convertedDf = NumericToVectorUtils
        .convertDataFrame(dataFrame, inputColumn, context)

      val transformedDf = transformer.transform(convertedDf)

      val expectedSchema        =
        transformSingleColumnSchema(inputColumn, outputColumn, dataFrame.schema.get).get
      val revertedTransformedDf =
        NumericToVectorUtils.revertDataFrame(transformedDf, expectedSchema, inputColumn, outputColumn, context,
          convertOutputVectorToDouble)

      DataFrame.fromSparkDataFrame(revertedTransformedDf)
    } else
      // Input column type is vector
      DataFrame.fromSparkDataFrame(transformer.transform(dataFrame.sparkDataFrame))
  }

  override def transformSingleColumnSchema(
      inputColumn: String,
      outputColumn: String,
      schema: StructType
  ): Option[StructType] = {
    val transformer = sparkTransformerWithParams(schema)
    transformer.setInputCol(inputColumn)
    transformer.setOutputCol(outputColumn)
    try {
      if (
        convertInputNumericToVector
        && NumericToVectorUtils.isColumnNumeric(schema, inputColumn)
      ) {
        // Automatically convert numeric input column to one-element vector column
        val convertedSchema =
          NumericToVectorUtils.convertSchema(schema, inputColumn)

        val transformedSchema = transformer.transformSchema(convertedSchema)

        val revertedTransformedSchema =
          NumericToVectorUtils.revertSchema(transformedSchema, inputColumn, outputColumn, convertOutputVectorToDouble)
        Some(revertedTransformedSchema)
      } else {
        // Input column type is vector
        val transformedSchema = transformer.transformSchema(schema)
        Some(transformedSchema)
      }
    } catch {
      case e: Exception => throw new SparkTransformSchemaException(e)
    }
  }

  private def sparkTransformerWithParams(schema: StructType) =
    sparkTransformer.copy(sparkParamMap(sparkTransformer, schema)).asInstanceOf[T]

}
