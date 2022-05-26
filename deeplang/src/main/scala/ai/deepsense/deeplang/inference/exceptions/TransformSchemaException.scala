package ai.deepsense.deeplang.inference.exceptions

import ai.deepsense.deeplang.exceptions.DeepLangException

class TransformSchemaException(message: String, cause: Option[Exception] = None)
    extends DeepLangException(message, cause.orNull)
