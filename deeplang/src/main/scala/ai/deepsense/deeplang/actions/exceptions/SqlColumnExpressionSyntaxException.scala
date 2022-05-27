package ai.deepsense.deeplang.actions.exceptions

import ai.deepsense.deeplang.exceptions.DeepLangException

case class SqlColumnExpressionSyntaxException(formula: String)
    extends DeepLangException(s"Formula: '$formula' is not a valid Spark SQL expression")
