package ai.deepsense.deeplang.actions.exceptions

import ai.deepsense.deeplang.exceptions.FlowException

class UnknownOperationExecutionException extends FlowException("The operation is unknown and can't be executed")
