package ai.deepsense.deeplang.actionobjects

import ai.deepsense.deeplang.ActionExecutionDispatcher.Result
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasIsLargerBetterParam
import ai.deepsense.deeplang.actions.exceptions.CustomOperationExecutionException
import ai.deepsense.deeplang.parameters.CodeSnippetParameter
import ai.deepsense.deeplang.parameters.StringParameter
import ai.deepsense.deeplang.parameters.Parameter

abstract class CustomCodeEvaluator() extends Evaluator with HasIsLargerBetterParam {

  val InputPortNumber: Int = 0

  val OutputPortNumber: Int = 0

  val metricName = StringParameter(name = "metric name", description = None)

  setDefault(metricName -> "custom metric")

  def getMetricName: String = $(metricName)

  val codeParameter: CodeSnippetParameter

  override def params: Array[Parameter[_]] =
    Array(metricName, codeParameter, isLargerBetterParam)

  override def isLargerBetter: Boolean = $(isLargerBetterParam)

  def isValid(context: ExecutionContext, code: String): Boolean

  def runCode(context: ExecutionContext, code: String): Result

  def getComposedCode(userCode: String): String

  override def _evaluate(ctx: ExecutionContext, df: DataFrame): MetricValue = {
    val userCode = $(codeParameter)

    val composedCode = getComposedCode(userCode)

    if (!isValid(ctx, composedCode))
      throw CustomOperationExecutionException("Code validation failed")

    ctx.dataFrameStorage.withInputDataFrame(InputPortNumber, df.sparkDataFrame) {
      runCode(ctx, composedCode) match {
        case Left(error) =>
          throw CustomOperationExecutionException(s"Execution exception:\n\n$error")

        case Right(_) =>
          val sparkDataFrame =
            ctx.dataFrameStorage.getOutputDataFrame(OutputPortNumber).getOrElse {
              throw CustomOperationExecutionException(
                "Function `evaluate` finished successfully, but did not produce a metric."
              )
            }

          val metricValue = sparkDataFrame.collect.head.getAs[Double](0)
          MetricValue(getMetricName, metricValue)
      }
    }
  }

  override def _infer(k: Knowledge[DataFrame]): MetricValue =
    MetricValue.forInference(getMetricName)

}
