package io.deepsense.deeplang.doperables

import io.deepsense.deeplang.OperationExecutionDispatcher.Result
import io.deepsense.deeplang.DKnowledge
import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.HasIsLargerBetterParam
import io.deepsense.deeplang.doperations.exceptions.CustomOperationExecutionException
import io.deepsense.deeplang.params.CodeSnippetParam
import io.deepsense.deeplang.params.StringParam
import io.deepsense.deeplang.params.Param

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
