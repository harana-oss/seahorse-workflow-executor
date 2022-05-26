package io.deepsense.deeplang.inference.exceptions

import io.deepsense.deeplang.exceptions.DeepLangException

case class AllTypesNotCompilableException(portIndex: Int)
    extends DeepLangException(s"None of inferred types can be placed in the port $portIndex")
