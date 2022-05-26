package ai.deepsense.deeplang.inference.exceptions

import ai.deepsense.deeplang.exceptions.DeepLangException

case class AllTypesNotCompilableException(portIndex: Int)
    extends DeepLangException(s"None of inferred types can be placed in the port $portIndex")
