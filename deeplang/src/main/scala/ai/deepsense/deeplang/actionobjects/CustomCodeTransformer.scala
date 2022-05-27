package ai.deepsense.deeplang.actionobjects

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.ActionExecutionDispatcher.Result
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.exceptions.CustomOperationExecutionException
import ai.deepsense.deeplang.parameters.CodeSnippetParameter

abstract class CustomCodeTransformer extends Transformer {

  val InputPortNumber: Int = 0

  val OutputPortNumber: Int = 0

  val codeParameter: CodeSnippetParameter

  def getCodeParameter: String = $(codeParameter)

  def setCodeParameter(value: String): this.type = set(codeParameter, value)

  override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array(codeParameter)

  def isValid(context: ExecutionContext, code: String): Boolean

  def runCode(context: ExecutionContext, code: String): Result

  override def applyTransform(ctx: ExecutionContext, df: DataFrame): DataFrame = {
    val code = $(codeParameter)

    if (!isValid(ctx, code))
      throw CustomOperationExecutionException("Code validation failed")

    ctx.dataFrameStorage.withInputDataFrame(InputPortNumber, df.sparkDataFrame) {
      runCode(ctx, code) match {
        case Left(error) =>
          throw CustomOperationExecutionException(s"Execution exception:\n\n$error")

        case Right(_) =>
          val sparkDataFrame =
            ctx.dataFrameStorage.getOutputDataFrame(OutputPortNumber).getOrElse {
              throw CustomOperationExecutionException(
                "Operation finished successfully, but did not produce a DataFrame."
              )
            }

          DataFrame.fromSparkDataFrame(sparkDataFrame)
      }
    }
  }

}
