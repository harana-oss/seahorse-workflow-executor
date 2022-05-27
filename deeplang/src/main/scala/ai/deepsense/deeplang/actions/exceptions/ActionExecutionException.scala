package ai.deepsense.deeplang.actions.exceptions

import ai.deepsense.deeplang.exceptions.FlowException

abstract class ActionExecutionException(message: String, cause: Option[Throwable])
    extends FlowException(message, cause.orNull)
