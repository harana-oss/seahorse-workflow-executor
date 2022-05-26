package ai.deepsense.deeplang.inference.warnings

import ai.deepsense.deeplang.inference.InferenceWarning

case class SomeTypesNotCompilableWarning(portIndex: Int)
    extends InferenceWarning(s"Not all of inferred types can be placed in the port $portIndex")
