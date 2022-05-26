package io.deepsense.deeplang.doperables

import java.util.UUID

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.OperationExecutionDispatcher.Result
import io.deepsense.deeplang.params.CodeSnippetLanguage
import io.deepsense.deeplang.params.CodeSnippetParam
import io.deepsense.deeplang.params.Param
import org.apache.spark.sql.types.DataType

class RColumnTransformer() extends CustomCodeColumnTransformer {

  override val codeParameter = CodeSnippetParam(
    name = "column operation code",
    description = None,
    language = CodeSnippetLanguage(CodeSnippetLanguage.r)
  )

  setDefault(
    codeParameter ->
      """transform.column <- function(column, column.name) {
        |  return(column)
        |}""".stripMargin
  )

  override def getSpecificParams: Array[Param[_]] =
    Array(codeParameter, targetType)

  override def getComposedCode(
      userCode: String,
      inputColumn: String,
      outputColumn: String,
      targetType: DataType
  ): String = {
    val newFieldName = UUID.randomUUID().toString.replace("-", "")

    s"""
       |$userCode
       |
       |transform <- function(dataframe) {
       |  new.column <- cast(transform.column(dataframe$$'$inputColumn', '$inputColumn'),
       |    '${targetType.simpleString}')
       |  return(withColumn(dataframe, '$newFieldName', new.column))
       |}
    """.stripMargin
  }

  override def runCode(context: ExecutionContext, code: String): Result =
    context.customCodeExecutor.runR(code)

  override def isValid(context: ExecutionContext, code: String): Boolean =
    context.customCodeExecutor.isRValid(code)

}
