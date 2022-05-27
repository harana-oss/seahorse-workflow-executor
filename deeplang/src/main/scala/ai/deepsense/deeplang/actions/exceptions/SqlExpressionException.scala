package ai.deepsense.deeplang.actions.exceptions

import ai.deepsense.deeplang.exceptions.FlowException

case class SqlExpressionException(formula: String, errorText: String)
    extends FlowException(s"SQL formula '$formula' cannot be evaluated ($errorText)")
