package io.deepsense.deeplang.doperables

import io.deepsense.deeplang.OperationExecutionDispatcher.Result
import io.deepsense.deeplang._
import io.deepsense.deeplang.params.CodeSnippetLanguage
import io.deepsense.deeplang.params.CodeSnippetParam

case class PythonEvaluator() extends CustomCodeEvaluator {

  override val codeParameter = CodeSnippetParam(
    name = "python evaluator code",
    description = None,
    language = CodeSnippetLanguage(CodeSnippetLanguage.python)
  )

  setDefault(
    codeParameter ->
      """from math import sqrt
        |from operator import add
        |
        |def evaluate(dataframe):
        |    # Example Root-Mean-Square Error implementation
        |    n = dataframe.count()
        |    row_to_sq_error = lambda row: (row['label'] - row['prediction'])**2
        |    sum_sq_error = dataframe.rdd.map(row_to_sq_error).reduce(add)
        |    rmse = sqrt(sum_sq_error / n)
        |    return rmse""".stripMargin
  )

  override def runCode(context: ExecutionContext, code: String): Result =
    context.customCodeExecutor.runPython(code)

  override def isValid(context: ExecutionContext, code: String): Boolean =
    context.customCodeExecutor.isPythonValid(code)

  // Creating a dataframe is a workaround. Currently we can pass to jvm DataFrames only.
  // TODO DS-3695 Fix a metric value - dataframe workaround.
  override def getComposedCode(userCode: String): String =
    s"""
       |$userCode
       |
       |def transform(dataframe):
       |    result = evaluate(dataframe)
       |    try:
       |        float_result = float(result)
       |    except ValueError:
       |        raise Exception("Invalid result of `evaluate` function. " +
       |                        "Value " + str(result) + " of type " +
       |                        str(type(result)) + " cannot be converted to float.")
       |    except TypeError:
       |        raise Exception("Invalid result type of `evaluate` function. " +
       |                        "Type " + str(type(result)) +
       |                        " cannot be cast to float.")
       |    result_df = spark.createDataFrame([[float_result]])
       |    return result_df
      """.stripMargin

}
