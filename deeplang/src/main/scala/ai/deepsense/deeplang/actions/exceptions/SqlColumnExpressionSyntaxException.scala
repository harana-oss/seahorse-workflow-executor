package ai.deepsense.deeplang.actions.exceptions

import ai.deepsense.deeplang.exceptions.FlowException

case class SqlColumnExpressionSyntaxException(formula: String)
    extends FlowException(s"Formula: '$formula' is not a valid Spark SQL expression")
