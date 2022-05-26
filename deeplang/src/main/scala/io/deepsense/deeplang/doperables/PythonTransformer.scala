package io.deepsense.deeplang.doperables

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.OperationExecutionDispatcher.Result
import io.deepsense.deeplang.params.CodeSnippetLanguage
import io.deepsense.deeplang.params.CodeSnippetParam

class PythonTransformer extends CustomCodeTransformer {

  override lazy val codeParameter = CodeSnippetParam(
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
