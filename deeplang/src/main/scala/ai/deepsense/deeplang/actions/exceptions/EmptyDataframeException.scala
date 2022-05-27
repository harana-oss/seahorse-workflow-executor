package ai.deepsense.deeplang.actions.exceptions

import ai.deepsense.deeplang.exceptions.DeepLangException

case object EmptyDataframeException
    extends DeepLangException(
      message = "DataFrame cannot be empty."
    )
