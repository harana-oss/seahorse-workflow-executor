package ai.deepsense.deeplang.doperables

import ai.deepsense.deeplang.OperationExecutionDispatcher.Result
import ai.deepsense.deeplang.DKnowledge
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasIsLargerBetterParam
import ai.deepsense.deeplang.doperations.exceptions.CustomOperationExecutionException
import ai.deepsense.deeplang.params.CodeSnippetParam
import ai.deepsense.deeplang.params.StringParam
import ai.deepsense.deeplang.params.Param

abstract class CustomCodeEvaluator() extends Evaluator with HasIsLargerBetterParam {

  val InputPortNumber: Int = 0

  val OutputPortNumber: Int = 0

  val metricName = StringParam(name = "metric name", description = None)

  setDefault(metricName -> "custom metric")

  def getMetricName: String = $(metricName)

  val codeParameter: CodeSnippetParam

  override def params: Array[Param[_]] =
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

  override def _infer(k: DKnowledge[DataFrame]): MetricValue =
    MetricValue.forInference(getMetricName)

}
