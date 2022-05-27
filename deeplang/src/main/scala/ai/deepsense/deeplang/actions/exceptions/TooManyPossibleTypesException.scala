package ai.deepsense.deeplang.actions.exceptions

import ai.deepsense.deeplang.exceptions.FlowException

case class TooManyPossibleTypesException()
    extends FlowException(
      "There is too many possible types. " +
        "Parameters can not be fully validated."
    )
