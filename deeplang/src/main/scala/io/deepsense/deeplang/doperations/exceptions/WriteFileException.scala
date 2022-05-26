package io.deepsense.deeplang.doperations.exceptions

import scala.compat.Platform.EOL

import org.apache.spark.SparkException

import io.deepsense.commons.exception.DeepSenseException
import io.deepsense.commons.exception.FailureCode

case class WriteFileException(path: String, e: SparkException)
    extends DeepSenseException(
      code = FailureCode.NodeFailure,
      title = "WriteFileException",
      message = s"Unable to write file: $path",
      cause = Some(e),
      details = Map(
        "stacktrace" -> (e.getMessage + EOL + EOL + e.getStackTrace.mkString("", EOL, EOL))
      )
    )
