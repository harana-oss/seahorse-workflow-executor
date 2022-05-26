package io.deepsense.deeplang.params.exceptions

import io.deepsense.deeplang.exceptions.DeepLangException

case class NoArgumentConstructorRequiredException(val className: String) extends DeepLangException(className)
