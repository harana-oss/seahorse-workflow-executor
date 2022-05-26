package io.deepsense.deeplang.doperations

import scala.reflect.runtime.{universe => ru}

import org.apache.spark.sql
import org.apache.spark.sql.types.StructType

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.exceptions.DeepLangException
import io.deepsense.deeplang.inference.InferenceWarnings
import io.deepsense.deeplang.inference.SqlSchemaInferrer
import io.deepsense.deeplang.params.exceptions.ParamsEqualException
import io.deepsense.deeplang.params.CodeSnippetLanguage
import io.deepsense.deeplang.params.CodeSnippetParam
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.StringParam
import io.deepsense.deeplang.DOperation2To1
import io.deepsense.deeplang.DPortPosition
import io.deepsense.deeplang.DataFrame2To1Operation
import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.DPortPosition.DPortPosition
import io.deepsense.sparkutils.SQL
import io.deepsense.sparkutils.SparkSQLSession

final class SqlCombine
    extends DOperation2To1[DataFrame, DataFrame, DataFrame]
    with DataFrame2To1Operation
    with OperationDocumentation {

  override val id: Id = "8f254d75-276f-48b7-872d-e4a18b6a86c6"

  override val name: String = "SQL Combine"

  override val description: String = "Combines two DataFrames into one using custom SQL"

  override def inPortsLayout: Vector[DPortPosition] =
    Vector(DPortPosition.Left, DPortPosition.Right)

  val leftTableName = StringParam(
    name = "Left dataframe id",
    description = Some(
      "The identifier that can be used in the Spark SQL expression to refer the " +
        "left-hand side DataFrame."
    )
  )

  setDefault(leftTableName, "")

  def getLeftTableName: String = $(leftTableName)

  def setLeftTableName(name: String): this.type = set(leftTableName, name)

  val rightTableName = StringParam(
    name = "Right dataframe id",
    description = Some(
      "The identifier that can be used in the Spark SQL expression to refer the " +
        "right-hand side DataFrame."
    )
  )

  setDefault(rightTableName, "")

  def getRightTableName: String = $(rightTableName)

  def setRightTableName(name: String): this.type = set(rightTableName, name)

  val sqlCombineExpression = CodeSnippetParam(
    name = "expression",
    description = Some("SQL expression to be executed on two DataFrames, yielding a DataFrame."),
    language = CodeSnippetLanguage(CodeSnippetLanguage.sql)
  )

  setDefault(sqlCombineExpression, "")

  def getSqlCombineExpression: String = $(sqlCombineExpression)

  def setSqlCombineExpression(expression: String): this.type = set(sqlCombineExpression, expression)

  override protected def execute(left: DataFrame, right: DataFrame)(ctx: ExecutionContext): DataFrame = {
    logger.debug(
      s"SqlCombine(expression = '$getSqlCombineExpression', " +
        s"leftTableName = '$getLeftTableName', " +
        s"rightTableName = '$getRightTableName')"
    )
    val localSparkSQLSession = ctx.sparkSQLSession.newSession()
    val leftDf               = moveToSparkSQLSession(left.sparkDataFrame, localSparkSQLSession)
    val rightDf              = moveToSparkSQLSession(right.sparkDataFrame, localSparkSQLSession)

    SQL.registerTempTable(leftDf, getLeftTableName)
    SQL.registerTempTable(rightDf, getRightTableName)
    logger.debug(
      s"Tables '$getLeftTableName', '$getRightTableName' registered. " +
        s"Executing the expression"
    )
    val localSqlResult = localSparkSQLSession.sql(getSqlCombineExpression)
    val sqlResult      = moveToSparkSQLSession(localSqlResult, ctx.sparkSQLSession)
    DataFrame.fromSparkDataFrame(sqlResult)
  }

  override protected def inferSchema(leftSchema: StructType, rightSchema: StructType): (StructType, InferenceWarnings) =
    new SqlSchemaInferrer().inferSchema(
      getSqlCombineExpression,
      (getLeftTableName, leftSchema),
      (getRightTableName, rightSchema)
    )

  override protected def customValidateParams: Vector[DeepLangException] =
    if (getLeftTableName == getRightTableName)
      ParamsEqualException(
        firstParamName = "left dataframe id",
        secondParamName = "right dataframe id",
        value = getLeftTableName
      ).toVector
    else
      Vector.empty

  private def moveToSparkSQLSession(df: sql.DataFrame, destinationCtx: SparkSQLSession): sql.DataFrame =
    destinationCtx.createDataFrame(df.rdd, df.schema)

  override def params: Array[Param[_]] =
    Array(leftTableName, rightTableName, sqlCombineExpression)

  override def since: Version = Version(1, 4, 0)

  @transient
  override lazy val tTagTI_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

  @transient
  override lazy val tTagTI_1: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

  @transient
  override lazy val tTagTO_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

}
