package ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers

import org.apache.spark.ml.feature.{StopWordsRemover => SparkStopWordsRemover}
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.actionobjects.SparkTransformerAsMultiColumnTransformer
import ai.deepsense.deeplang.inference.exceptions.SparkTransformSchemaException
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.wrappers.spark.BooleanParameterWrapper

class StopWordsRemover extends SparkTransformerAsMultiColumnTransformer[SparkStopWordsRemover] {

  val caseSensitive = new BooleanParameterWrapper[SparkStopWordsRemover](
    name = "case sensitive",
    description = Some("Whether to do a case sensitive comparison over the stop words."),
    sparkParamGetter = _.caseSensitive
  )

  setDefault(caseSensitive, false)

  override protected def getSpecificParams: Array[Parameter[_]] = Array(caseSensitive)

  // TODO: This override will not be necessary after fixing StopWordsRemover.transformSchema
  //       in Apache Spark code
  override def transformSingleColumnSchema(
      inputColumn: String,
      outputColumn: String,
      schema: StructType
  ): Option[StructType] = {
    try {
      val inputFields = schema.fieldNames
      require(!inputFields.contains(outputColumn), s"Output column $outputColumn already exists.")
    } catch {
      case e: Exception => throw new SparkTransformSchemaException(e)
    }
    super.transformSingleColumnSchema(inputColumn, outputColumn, schema)
  }

}
