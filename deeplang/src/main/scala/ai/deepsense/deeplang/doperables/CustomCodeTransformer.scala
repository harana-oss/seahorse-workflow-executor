package ai.deepsense.deeplang.doperables

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.OperationExecutionDispatcher.Result
import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import ai.deepsense.deeplang.doperations.exceptions.CustomOperationExecutionException
import ai.deepsense.deeplang.params.CodeSnippetParam

abstract class CustomCodeTransformer extends Transformer {

  val InputPortNumber: Int = 0

  val OutputPortNumber: Int = 0

  val codeParameter: CodeSnippetParam

  def getCodeParameter: String = $(codeParameter)

  def setCodeParameter(value: String): this.type = set(codeParameter, value)

  override val params: Array[ai.deepsense.deeplang.params.Param[_]] = Array(codeParameter)

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
