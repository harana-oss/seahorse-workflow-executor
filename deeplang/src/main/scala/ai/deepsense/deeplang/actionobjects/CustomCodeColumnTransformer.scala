package ai.deepsense.deeplang.actionobjects

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.exceptions.CustomOperationExecutionException
import ai.deepsense.deeplang.ActionExecutionDispatcher.Result
import ai.deepsense.deeplang.parameters.CodeSnippetParameter
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.choice.ChoiceParameter
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

abstract class CustomCodeColumnTransformer() extends MultiColumnTransformer {

  import CustomCodeColumnTransformer._

  val targetType =
    ChoiceParameter[TargetTypeChoice](name = "target type", description = Some("Target type of the columns."))

  def getTargetType: TargetTypeChoice = $(targetType)

  def setTargetType(value: TargetTypeChoice): this.type = set(targetType, value)

  val codeParameter: CodeSnippetParameter

  def getCodeParameter: String = $(codeParameter)

  def setCodeParameter(value: String): this.type = set(codeParameter, value)

  def runCode(context: ExecutionContext, code: String): Result

  def isValid(context: ExecutionContext, code: String): Boolean

  def getComposedCode(userCode: String, inputColumn: String, outputColumn: String, targetType: DataType): String

  override def getSpecificParams: Array[Parameter[_]]

  private def executeCode(
      code: String,
      inputColumn: String,
      outputColumn: String,
      context: ExecutionContext,
      dataFrame: DataFrame
  ): DataFrame = {
    runCode(context, code) match {
      case Left(error) =>
        throw CustomOperationExecutionException(s"Execution exception:\n\n$error")

      case Right(_) =>
        val sparkDataFrame =
          context.dataFrameStorage.getOutputDataFrame(OutputPortNumber).getOrElse {
            throw CustomOperationExecutionException("Operation finished successfully, but did not produce a DataFrame.")
          }

        val newSparkDataFrame = context.sparkSQLSession.createDataFrame(
          sparkDataFrame.rdd,
          transformSingleColumnSchema(inputColumn, outputColumn, dataFrame.schema.get).get
        )
        DataFrame.fromSparkDataFrame(newSparkDataFrame)
    }
  }

  override def transformSingleColumn(
      inputColumn: String,
      outputColumn: String,
      context: ExecutionContext,
      dataFrame: DataFrame
  ): DataFrame = {
    val code = getComposedCode($(codeParameter), inputColumn, outputColumn, getTargetType.columnType)
    logger.debug(s"Code to be validated and executed:\n$code")

    if (!isValid(context, code))
      throw CustomOperationExecutionException("Code validation failed")

    context.dataFrameStorage.withInputDataFrame(InputPortNumber, dataFrame.sparkDataFrame) {
      executeCode(code, inputColumn, outputColumn, context, dataFrame)
    }
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

object CustomCodeColumnTransformer {

  val InputPortNumber: Int = 0

  val OutputPortNumber: Int = 0

}
