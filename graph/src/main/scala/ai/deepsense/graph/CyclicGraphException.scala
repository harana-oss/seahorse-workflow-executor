package ai.deepsense.graph

import ai.deepsense.commons.exception.HaranaException
import ai.deepsense.commons.exception.FailureCode

case class CyclicGraphException()
    extends HaranaException(FailureCode.IllegalArgumentException, "Cyclic graph", "Graph cycle detected")
