package ai.deepsense.deeplang.actionobjects.dataframe

import org.apache.spark.sql
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

import ai.deepsense.commons.types.ColumnType.ColumnType
import ai.deepsense.commons.types.SparkConversions
import ai.deepsense.deeplang.actionobjects.dataframe.report.DataFrameReportGenerator
import ai.deepsense.deeplang.actionobjects.descriptions.DataFrameInferenceResult
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actions.exceptions.BacktickInColumnNameException
import ai.deepsense.deeplang.actions.exceptions.DuplicatedColumnsException
import ai.deepsense.deeplang.actions.exceptions.WrongColumnTypeException
import ai.deepsense.deeplang.ActionObject
import ai.deepsense.deeplang.ExecutionContext

/** @param sparkDataFrame
  *   Spark representation of data. User of this class has to assure that sparkDataFrame data fulfills its internal
  *   schema.
  * @param schema
  *   Schema of the DataFrame. Usually it is schema of sparkDataFrame, but for inference, DataFrame may be not set but
  *   schema is known.
  */
case class DataFrame protected[dataframe] (sparkDataFrame: sql.DataFrame, schema: Option[StructType])
    extends ActionObject
    with DataFrameColumnsGetter {

  def this() = this(null, None)

  schema.foreach { struct =>
    val duplicatedColumnNames = struct.fieldNames.groupBy(identity).collect {
      case (col, list) if list.length > 1 => col
    }
    if (duplicatedColumnNames.nonEmpty)
      throw DuplicatedColumnsException(duplicatedColumnNames.toList)

    // We had to forbid backticks in column names due to anomalies in Spark 1.6
    // See: https://issues.apache.org/jira/browse/SPARK-13297
    val columnNamesWithBackticks = struct.fieldNames.groupBy(identity).collect {
      case (col, list) if col.contains("`") => col
    }
    if (columnNamesWithBackticks.nonEmpty)
      throw BacktickInColumnNameException(columnNamesWithBackticks.toList)
  }

  /** Creates new DataFrame with new columns added. */
  def withColumns(context: ExecutionContext, newColumns: Traversable[sql.Column]): DataFrame = {
    val columns: List[sql.Column] = new sql.ColumnName("*") :: newColumns.toList
    val newSparkDataFrame         = sparkDataFrame.select(columns: _*)
    DataFrame.fromSparkDataFrame(newSparkDataFrame)
  }

  override def report(extended: Boolean): Report = {
    extended match {
      case true  => DataFrameReportGenerator.report(sparkDataFrame)
      case false => DataFrameReportGenerator.schemaReport(sparkDataFrame)
    }
  }

  override def inferenceResult: Option[DataFrameInferenceResult] =
    schema.map(DataFrameInferenceResult)

}

object DataFrame {

  def apply(sparkDataFrame: sql.DataFrame, schema: StructType): DataFrame =
    DataFrame(sparkDataFrame, Some(schema))

  /** @return
    *   DataFrame object that can be used _only_ for inference,
    * i.e. it contains only schema of this DataFrame.
    */
  def forInference(schema: StructType): DataFrame = forInference(Some(schema))

  /** @return
    *   DataFrame object that can be used _only_ for inference,
    * i.e. it contains only schema of this DataFrame.
    */
  def forInference(schema: Option[StructType] = None): DataFrame = DataFrame(null, schema)

  /** Throws [[ai.deepsense.deeplang.actions.exceptions.WrongColumnTypeException WrongColumnTypeException]] if some
    * columns of schema have type different than one of expected.
    */
  def assertExpectedColumnType(schema: StructType, expectedTypes: ColumnType*): Unit =
    for (field <- schema.fields)
      assertExpectedColumnType(field, expectedTypes: _*)

  /** Throws [[ai.deepsense.deeplang.actions.exceptions.WrongColumnTypeException WrongColumnTypeException]] if
    * column has type different than one of expected.
    */
  def assertExpectedColumnType(column: StructField, expectedTypes: ColumnType*): Unit = {
    val actualType = SparkConversions.sparkColumnTypeToColumnType(column.dataType)
    if (!expectedTypes.contains(actualType))
      throw WrongColumnTypeException(column.name, actualType, expectedTypes: _*)
  }

  /** Generates a DataFrame with no columns. */
  def empty(context: ExecutionContext): DataFrame = {
    val emptyRdd            = context.sparkContext.parallelize(Seq[Row]())
    val emptySparkDataFrame = context.sparkSQLSession.createDataFrame(emptyRdd, StructType(Seq.empty))
    fromSparkDataFrame(emptySparkDataFrame)
  }

  def loadFromFs(context: ExecutionContext)(path: String): DataFrame = {
    val dataFrame = context.sparkSQLSession.read.parquet(path)
    fromSparkDataFrame(dataFrame)
  }

  def fromSparkDataFrame(sparkDataFrame: sql.DataFrame): DataFrame =
    DataFrame(sparkDataFrame, Some(sparkDataFrame.schema))

}
