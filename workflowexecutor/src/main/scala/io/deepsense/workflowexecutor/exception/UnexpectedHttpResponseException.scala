package io.deepsense.workflowexecutor.exception

import spray.http.StatusCode

case class UnexpectedHttpResponseException(val message: String, val statusCode: StatusCode, val content: String)
    extends Exception(s"$message: Unexpected HTTP response: $statusCode")
