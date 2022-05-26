package io.deepsense.deeplang

case class CustomCodeExecutionProvider(
    pythonCodeExecutor: CustomCodeExecutor,
    rCodeExecutor: CustomCodeExecutor,
    operationExecutionDispatcher: OperationExecutionDispatcher
)
