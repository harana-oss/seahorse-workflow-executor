package ai.deepsense.deeplang.params.exceptions

import ai.deepsense.deeplang.exceptions.DeepLangException

case class NoArgumentConstructorRequiredException(val className: String) extends DeepLangException(className)
