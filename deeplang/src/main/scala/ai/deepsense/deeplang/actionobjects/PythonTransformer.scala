package ai.deepsense.deeplang.actionobjects

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.ActionExecutionDispatcher.Result
import ai.deepsense.deeplang.parameters.CodeSnippetLanguage
import ai.deepsense.deeplang.parameters.CodeSnippetParameter

class PythonTransformer extends CustomCodeTransformer {

  override lazy val codeParameter = CodeSnippetParameter(
    name = "code",
    description = None,
    language = CodeSnippetLanguage(CodeSnippetLanguage.python)
  )

  setDefault(codeParameter -> "def transform(dataframe):\n    return dataframe")

  override def isValid(context: ExecutionContext, code: String): Boolean =
    context.customCodeExecutor.isPythonValid(code)

  override def runCode(context: ExecutionContext, code: String): Result =
    context.customCodeExecutor.runPython(code)

}
