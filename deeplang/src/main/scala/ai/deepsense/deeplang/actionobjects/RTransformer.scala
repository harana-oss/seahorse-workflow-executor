package ai.deepsense.deeplang.actionobjects

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.ActionExecutionDispatcher.Result
import ai.deepsense.deeplang.parameters.CodeSnippetLanguage
import ai.deepsense.deeplang.parameters.CodeSnippetParameter

class RTransformer extends CustomCodeTransformer {

  override lazy val codeParameter = CodeSnippetParameter(
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
