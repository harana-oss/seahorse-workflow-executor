package ai.deepsense.deeplang.actions.exceptions

import ai.deepsense.deeplang.exceptions.FlowException

case class HaranaUnknownHostException(e: Throwable) extends FlowException(s"Unknown host: ${e.getMessage}", e)
