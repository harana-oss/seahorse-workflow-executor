package ai.deepsense.deeplang.inference.exceptions

import ai.deepsense.deeplang.exceptions.FlowException

case class AllTypesNotCompilableException(portIndex: Int)
    extends FlowException(s"None of inferred types can be placed in the port $portIndex")
