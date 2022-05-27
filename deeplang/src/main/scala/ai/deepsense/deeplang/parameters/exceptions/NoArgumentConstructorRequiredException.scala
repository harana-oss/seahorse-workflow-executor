package ai.deepsense.deeplang.parameters.exceptions

import ai.deepsense.deeplang.exceptions.DeepLangException

case class NoArgumentConstructorRequiredException(val className: String) extends DeepLangException(className)
