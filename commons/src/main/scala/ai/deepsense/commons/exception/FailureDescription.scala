package ai.deepsense.commons.exception

import ai.deepsense.commons.exception.FailureCode.FailureCode

case class FailureDescription(
                               id: HaranaFile.Id,
                               code: FailureCode,
                               title: String,
                               message: Option[String] = None,
                               details: Map[String, String] = Map()
)

object FailureDescription {

  def stacktraceDetails(stackTrace: Array[StackTraceElement]): Map[String, String] = {
    import scala.compat.Platform.EOL
    Map("stacktrace" -> stackTrace.mkString("", EOL, EOL))
  }

}
