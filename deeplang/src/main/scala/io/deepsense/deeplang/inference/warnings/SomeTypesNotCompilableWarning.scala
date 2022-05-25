package io.deepsense.deeplang.inference.warnings

import io.deepsense.deeplang.inference.InferenceWarning

case class SomeTypesNotCompilableWarning(
    portIndex: Int)
  extends InferenceWarning(s"Not all of inferred types can be placed in the port $portIndex")
