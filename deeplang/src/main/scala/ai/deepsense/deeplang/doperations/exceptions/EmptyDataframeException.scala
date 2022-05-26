package ai.deepsense.deeplang.doperations.exceptions

import ai.deepsense.deeplang.exceptions.DeepLangException

case object EmptyDataframeException
    extends DeepLangException(
      message = "DataFrame cannot be empty."
    )
