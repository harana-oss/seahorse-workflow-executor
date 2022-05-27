package ai.deepsense.deeplang.parameters.exceptions

import ai.deepsense.deeplang.exceptions.FlowException

/** Base class for all Parameters Validation exceptions. */
abstract class ValidationException(message: String) extends FlowException(message)
