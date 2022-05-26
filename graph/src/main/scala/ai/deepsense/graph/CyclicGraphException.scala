package ai.deepsense.graph

import ai.deepsense.commons.exception.DeepSenseException
import ai.deepsense.commons.exception.FailureCode

case class CyclicGraphException()
    extends DeepSenseException(FailureCode.IllegalArgumentException, "Cyclic graph", "Graph cycle detected")
