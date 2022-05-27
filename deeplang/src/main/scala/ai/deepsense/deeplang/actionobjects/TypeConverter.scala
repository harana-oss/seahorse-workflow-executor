package ai.deepsense.deeplang.actionobjects

import org.apache.spark.sql.types._

import ai.deepsense.deeplang._
import ai.deepsense.deeplang.actionobjects.dataframe._
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.choice.ChoiceParameter

case class TypeConverter() extends MultiColumnTransformer {

  val targetType =
    ChoiceParameter[TargetTypeChoice](name = "target type", description = Some("Target type of the columns."))

  def getTargetType: TargetTypeChoice = $(targetType)

  def setTargetType(value: TargetTypeChoice): this.type = set(targetType, value)

  override def getSpecificParams: Array[Parameter[_]] = Array(targetType)

  override def transformSingleColumn(
      inputColumn: String,
      outputColumn: String,
      context: ExecutionContext,
      dataFrame: DataFrame
  ): DataFrame = {
    val targetTypeName = getTargetType.columnType.typeName
    val expr           = s"cast(`$inputColumn` as $targetTypeName) as `$outputColumn`"
    val sparkDataFrame = dataFrame.sparkDataFrame.selectExpr("*", expr)
    DataFrame.fromSparkDataFrame(sparkDataFrame)
  }

  override def transformSingleColumnSchema(
      inputColumn: String,
      outputColumn: String,
      schema: StructType
  ): Option[StructType] = {
    MultiColumnTransformer.assertColumnExist(inputColumn, schema)
    MultiColumnTransformer.assertColumnDoesNotExist(outputColumn, schema)
    Some(schema.add(StructField(outputColumn, getTargetType.columnType, nullable = true)))
  }

}
