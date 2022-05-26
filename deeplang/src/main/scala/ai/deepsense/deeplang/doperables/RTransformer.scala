package ai.deepsense.deeplang.doperables

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.OperationExecutionDispatcher.Result
import ai.deepsense.deeplang.params.CodeSnippetLanguage
import ai.deepsense.deeplang.params.CodeSnippetParam

class RTransformer extends CustomCodeTransformer {

  override lazy val codeParameter = CodeSnippetParam(
    name = "code",
    description = None,
    language = CodeSnippetLanguage(CodeSnippetLanguage.r)
  )

  setDefault(
    codeParameter ->
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
