package ai.deepsense.deeplang.actions.exceptions

import scala.compat.Platform.EOL

import org.apache.spark.SparkException

import ai.deepsense.commons.exception.HaranaException
import ai.deepsense.commons.exception.FailureCode

case class WriteFileException(path: String, e: SparkException)
    extends HaranaException(
      code = FailureCode.NodeFailure,
      title = "WriteFileException",
      message = s"Unable to write file: $path",
      cause = Some(e),
      details = Map(
        "stacktrace" -> (e.getMessage + EOL + EOL + e.getStackTrace.mkString("", EOL, EOL))
      )
    )
