package ai.deepsense.deeplang.parameters.exceptions

import ai.deepsense.deeplang.exceptions.DeepLangException

/** Base class for all Parameters Validation exceptions. */
abstract class ValidationException(message: String) extends DeepLangException(message)
