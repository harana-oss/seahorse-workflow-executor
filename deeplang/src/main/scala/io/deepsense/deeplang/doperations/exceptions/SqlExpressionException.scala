package io.deepsense.deeplang.doperations.exceptions

import io.deepsense.deeplang.exceptions.DeepLangException

case class SqlExpressionException(formula: String, errorText: String) extends DeepLangException(
  s"SQL formula '$formula' cannot be evaluated ($errorText)")
