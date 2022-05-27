package ai.deepsense.deeplang.actions.exceptions

import ai.deepsense.deeplang.exceptions.FlowException

case object EmptyDataframeException
    extends FlowException(
      message = "DataFrame cannot be empty."
    )
