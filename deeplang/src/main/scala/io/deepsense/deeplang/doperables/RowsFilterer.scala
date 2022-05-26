package io.deepsense.deeplang.doperables

import org.apache.spark.sql.types.StructType

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.params.CodeSnippetParam
import io.deepsense.deeplang.params.CodeSnippetLanguage
import io.deepsense.deeplang.params.Param
import io.deepsense.sparkutils.SQL

class RowsFilterer extends Transformer {

  val condition = CodeSnippetParam(
    name = "condition",
    description = Some(
      "Condition used to filter rows. " +
        "Only rows that satisfy condition will remain in DataFrame. Use SQL syntax."
    ),
    language = CodeSnippetLanguage(CodeSnippetLanguage.sql)
  )

  def getCondition: String = $(condition)

  def setCondition(value: String): this.type = set(condition, value)

  override val params: Array[Param[_]] = Array(condition)

  override private[deeplang] def _transform(ctx: ExecutionContext, df: DataFrame): DataFrame = {
    val uniqueDataFrameId   = "row_filterer_" + java.util.UUID.randomUUID.toString.replace('-', '_')
    val resultantExpression = s"SELECT * FROM $uniqueDataFrameId WHERE $getCondition"
    logger.debug(
      s"RowsFilterer(expression = 'resultantExpression'," +
        s" uniqueDataFrameId = '$uniqueDataFrameId')"
    )

    SQL.registerTempTable(df.sparkDataFrame, uniqueDataFrameId)
    try {
      logger.debug(s"Table '$uniqueDataFrameId' registered. Executing the expression")
      val sqlResult = SQL.sparkSQLSession(df.sparkDataFrame).sql(resultantExpression)
      DataFrame.fromSparkDataFrame(sqlResult)
    } finally {
      logger.debug(s"Unregistering the temporary table '$uniqueDataFrameId'")
      SQL.sparkSQLSession(df.sparkDataFrame).dropTempTable(uniqueDataFrameId)
    }
  }

  override private[deeplang] def _transformSchema(schema: StructType): Option[StructType] =
    Some(schema)

}
