package io.deepsense.deeplang.doperations.exceptions

import io.deepsense.deeplang.exceptions.DeepLangException

case object EmptyDataframeException
    extends DeepLangException(
      message = "DataFrame cannot be empty."
    )
