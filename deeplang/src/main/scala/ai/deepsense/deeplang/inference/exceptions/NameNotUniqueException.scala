package ai.deepsense.deeplang.inference.exceptions

import ai.deepsense.deeplang.exceptions.DeepLangException

case class NameNotUniqueException(name: String) extends DeepLangException(s"Name '$name' is not unique")
