package io.deepsense.deeplang.doperations.exceptions

import io.deepsense.deeplang.exceptions.DeepLangException

case class ColumnAliasNotUniqueException(alias: String) extends DeepLangException(
  s"Alias '$alias' is not unique within the input DataFrame")
