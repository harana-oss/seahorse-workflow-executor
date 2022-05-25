package io.deepsense.deeplang.doperables

import org.apache.spark.sql
import org.apache.spark.sql.types.StructType

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.exceptions.SqlExpressionException
import io.deepsense.deeplang.inference.{SqlInferenceWarning, SqlSchemaInferrer}
import io.deepsense.deeplang.params.{CodeSnippetLanguage, CodeSnippetParam, Param, StringParam}
import io.deepsense.sparkutils.SparkSQLSession
import io.deepsense.sparkutils.SQL

class SqlTransformer extends Transformer {

  val dataFrameId = StringParam(
    name = "dataframe id",
    description = Some("An identifier that can be used in " +
      "the SQL expression to refer to the input DataFrame."))
  setDefault(dataFrameId -> "df")
  def getDataFrameId: String = $(dataFrameId)
  def setDataFrameId(value: String): this.type = set(dataFrameId, value)

  val expression = CodeSnippetParam(
    name = "expression",
    description = Some("SQL Expression to be executed on the DataFrame."),
    language = CodeSnippetLanguage(CodeSnippetLanguage.sql))
  setDefault(expression -> "SELECT * FROM df")
  def getExpression: String = $(expression)
  def setExpression(value: String): this.type = set(expression, value)

  override val params: Array[Param[_]] = Array(dataFrameId, expression)

  override private[deeplang] def _transform(ctx: ExecutionContext, df: DataFrame): DataFrame = {
    logger.debug(s"SqlExpression(expression = '$getExpression'," +
      s" dataFrameId = '$getDataFrameId')")

    val localSparkSQLSession = ctx.sparkSQLSession.newSession()
    val localDataFrame = moveToSparkSQLSession(df.sparkDataFrame, localSparkSQLSession)

    SQL.registerTempTable(localDataFrame, getDataFrameId)
    try {
      logger.debug(s"Table '$dataFrameId' registered. Executing the expression")
      val sqlResult = moveToSparkSQLSession(localSparkSQLSession.sql(getExpression), ctx.sparkSQLSession)
      DataFrame.fromSparkDataFrame(sqlResult)
    } finally {
      logger.debug("Unregistering the temporary table " + getDataFrameId)
      localSparkSQLSession.dropTempTable(getDataFrameId)
    }
  }

  override private[deeplang] def _transformSchema(schema: StructType): Option[StructType] = {
    val (resultSchema, warnings) =
      new SqlSchemaInferrer().inferSchema(getExpression, (getDataFrameId, schema))
    // We throw/log as there is no way to pass warnings further at this point.
    warnings.warnings.foreach {
      case SqlInferenceWarning(sqlExpression, warningText) =>
        throw SqlExpressionException(sqlExpression, warningText)
      case other => logger.warn(s"Inference warning not reported: ${other.message}")
    }
    Some(resultSchema)
  }

  private def moveToSparkSQLSession(df: sql.DataFrame, destinationSession: SparkSQLSession): sql.DataFrame =
    destinationSession.createDataFrame(df.rdd, df.schema)
}
