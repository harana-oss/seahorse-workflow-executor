package io.deepsense.deeplang.doperations.exceptions

import io.deepsense.deeplang.exceptions.DeepLangException

class UnknownOperationExecutionException extends DeepLangException(
  "The operation is unknown and can't be executed")
