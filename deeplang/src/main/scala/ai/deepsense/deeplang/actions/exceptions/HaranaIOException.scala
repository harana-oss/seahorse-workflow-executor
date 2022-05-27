package ai.deepsense.deeplang.actions.exceptions

import ai.deepsense.deeplang.exceptions.FlowException

case class HaranaIOException(e: Throwable) extends FlowException(s"Harana IO Exception: ${e.getMessage}", e)
