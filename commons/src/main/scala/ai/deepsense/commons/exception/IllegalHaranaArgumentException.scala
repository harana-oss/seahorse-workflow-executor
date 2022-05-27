package ai.deepsense.commons.exception

class IllegalHaranaArgumentException(message: String)
    extends HaranaException(
      FailureCode.IllegalArgumentException,
      "Illegal Harana argument exception",
      message,
      None
    )
