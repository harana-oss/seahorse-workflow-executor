package ai.deepsense.deeplang.parameters.exceptions

import ai.deepsense.deeplang.exceptions.FlowException

case class NoArgumentConstructorRequiredException(val className: String) extends FlowException(className)
