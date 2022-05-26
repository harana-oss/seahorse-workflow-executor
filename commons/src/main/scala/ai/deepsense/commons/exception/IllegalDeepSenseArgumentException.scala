package ai.deepsense.commons.exception

class IllegalDeepSenseArgumentException(message: String)
    extends DeepSenseException(
      FailureCode.IllegalArgumentException,
      "Illegal DeepSense argument exception",
      message,
      None
    )
