package io.deepsense.graph

import io.deepsense.commons.exception.{DeepSenseException, FailureCode}

case class CyclicGraphException()
  extends DeepSenseException(
    FailureCode.IllegalArgumentException,
    "Cyclic graph",
    "Graph cycle detected")
