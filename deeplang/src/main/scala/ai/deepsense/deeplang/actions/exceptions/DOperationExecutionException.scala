package ai.deepsense.deeplang.actions.exceptions

import ai.deepsense.deeplang.exceptions.DeepLangException

abstract class DOperationExecutionException(message: String, cause: Option[Throwable])
    extends DeepLangException(message, cause.orNull)
