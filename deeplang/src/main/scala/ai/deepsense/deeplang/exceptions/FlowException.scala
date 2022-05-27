package ai.deepsense.deeplang.exceptions

import ai.deepsense.commons.exception.FailureDescription
import ai.deepsense.commons.exception.FailureCode
import ai.deepsense.commons.exception.HaranaException

class FlowException(override val message: String, cause: Throwable = null)
    extends HaranaException(
      FailureCode.NodeFailure,
      "DeepLang Exception",
      message,
      Option(cause),
      Option(cause)
        .map(e => FailureDescription.stacktraceDetails(e.getStackTrace))
        .getOrElse(Map())
    ) {

  def toVector: Vector[FlowException] = Vector(this)

}
