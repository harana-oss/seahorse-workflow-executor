package ai.deepsense.deeplang.actionobjects

import org.apache.spark.sql.Row
import org.apache.spark.sql.types._

import ai.deepsense.commons.types.ColumnType
import ai.deepsense.deeplang._
import ai.deepsense.deeplang.actionobjects.dataframe._
import ai.deepsense.deeplang.parameters.NumericParameter
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.validators.RangeValidator

case class GetFromVectorTransformer() extends MultiColumnTransformer {

  val index = NumericParameter(
    name = "index",
    description = Some("Index of value to extract (starting from 0)."),
    validator = RangeValidator.positiveIntegers
  )

  setDefault(index, 0.0)

  def getIndex: Int = $(index).toInt

  def setIndex(value: Double): this.type = set(index, value)

  override def getSpecificParams: Array[Parameter[_]] = Array(index)

  override def transformSingleColumn(
      inputColumn: String,
      outputColumn: String,
      context: ExecutionContext,
      dataFrame: DataFrame
  ): DataFrame = {
    val inputColumnIndex     = dataFrame.schema.get.fieldIndex(inputColumn)
    val indexInVector        = getIndex
    val transformedRdd       = dataFrame.sparkDataFrame.rdd.map { case r =>
      val vector = r.get(inputColumnIndex).asInstanceOf[ai.deepsense.sparkutils.Linalg.Vector]
      // Append output column as the last column
      if (vector != null)
        Row.fromSeq(r.toSeq :+ vector.apply(indexInVector))
      else
        Row.fromSeq(r.toSeq :+ null)
    }
    val expectedSchema       =
      transformSingleColumnSchema(inputColumn, outputColumn, dataFrame.schema.get).get
    val transformedDataFrame = context.sparkSQLSession.createDataFrame(transformedRdd, expectedSchema)
    DataFrame.fromSparkDataFrame(transformedDataFrame)
  }

  override def transformSingleColumnSchema(
      inputColumn: String,
      outputColumn: String,
      schema: StructType
  ): Option[StructType] = {
    MultiColumnTransformer.assertColumnExist(inputColumn, schema)
    DataFrame.assertExpectedColumnType(schema(inputColumn), ColumnType.vector)
    MultiColumnTransformer.assertColumnDoesNotExist(outputColumn, schema)
    Some(schema.add(StructField(outputColumn, DoubleType)))
  }

}
