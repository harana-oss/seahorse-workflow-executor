package io.deepsense.deeplang.inference.exceptions

import io.deepsense.deeplang.exceptions.DeepLangException

class TransformSchemaException(message: String, cause: Option[Exception] = None)
    extends DeepLangException(message, cause.orNull)
