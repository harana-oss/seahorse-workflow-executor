package ai.deepsense.deeplang.params.exceptions

import ai.deepsense.deeplang.exceptions.DeepLangException

/** Base class for all Parameters Validation exceptions. */
abstract class ValidationException(message: String) extends DeepLangException(message)
