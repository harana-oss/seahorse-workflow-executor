package io.deepsense.deeplang.doperations.exceptions

import io.deepsense.deeplang.exceptions.DeepLangException

case class SqlColumnExpressionSyntaxException(formula: String) extends DeepLangException(
  s"Formula: '$formula' is not a valid Spark SQL expression")
