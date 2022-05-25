package io.deepsense.deeplang.doperables

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.OperationExecutionDispatcher.Result
import io.deepsense.deeplang.params.{CodeSnippetLanguage, CodeSnippetParam}

class RTransformer extends CustomCodeTransformer {

  override lazy val codeParameter = CodeSnippetParam(
    name = "code",
    description = None,
    language = CodeSnippetLanguage(CodeSnippetLanguage.r)
  )
  setDefault(codeParameter ->
    """transform <- function(dataframe) {
      |  return(dataframe)
      |}
    """.stripMargin
  )

  override def isValid(context: ExecutionContext, code: String): Boolean =
    context.customCodeExecutor.isRValid(code)

  override def runCode(context: ExecutionContext, code: String): Result =
    context.customCodeExecutor.runR(code)
}
