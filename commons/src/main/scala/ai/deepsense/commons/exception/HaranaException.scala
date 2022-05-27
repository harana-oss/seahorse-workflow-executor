package ai.deepsense.commons.exception

import ai.deepsense.commons.exception.FailureCode.FailureCode

/** Base exception for all Harana exceptions */
abstract class HaranaException(
    val code: FailureCode,
    val title: String,
    val message: String,
    val cause: Option[Throwable] = None,
    val details: Map[String, String] = Map()
) extends Exception(message, cause.orNull) {

  val id = HaranaFile.Id.randomId

  def failureDescription: FailureDescription =
    FailureDescription(id, code, title, Some(message), details ++ additionalDetails)

  protected def additionalDetails: Map[String, String] = Map()

}
