package ai.deepsense.deeplang.actions.exceptions

import ai.deepsense.deeplang.exceptions.FlowException

case class ColumnAliasNotUniqueException(alias: String)
    extends FlowException(s"Alias '$alias' is not unique within the input DataFrame")
