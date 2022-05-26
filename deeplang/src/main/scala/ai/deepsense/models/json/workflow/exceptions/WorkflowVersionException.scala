package ai.deepsense.models.json.workflow.exceptions

import ai.deepsense.commons.exception.DeepSenseException
import ai.deepsense.commons.exception.FailureCode
import ai.deepsense.commons.utils.Version

sealed abstract class WorkflowVersionException(
    title: String,
    message: String,
    cause: Option[Throwable] = None,
    details: Map[String, String] = Map()
) extends DeepSenseException(FailureCode.IncorrectWorkflow, title, message, cause, details)

case class WorkflowVersionFormatException(stringVersion: String)
    extends WorkflowVersionException(
      "Workflow's version has wrong format. ",
      "Workflow's version has wrong format. " +
        s"Got '$stringVersion' but expected X.Y.Z " +
        "where X, Y, Z are positive integers"
    )

case class WorkflowVersionNotFoundException(supportedApiVersion: Version)
    extends WorkflowVersionException(
      "API version was not included in the request.",
      "API version was not included in the request." +
        s"Currently supported version is ${supportedApiVersion.humanReadable}"
    ) {

  override protected def additionalDetails: Map[String, String] =
    Map("supportedApiVersion" -> supportedApiVersion.humanReadable)

}

case class WorkflowVersionNotSupportedException(workflowApiVersion: Version, supportedApiVersion: Version)
    extends WorkflowVersionException(
      s"API version ${workflowApiVersion.humanReadable} is not supported.",
      s"API version ${workflowApiVersion.humanReadable} is not supported. " +
        s"Currently supported version is ${supportedApiVersion.humanReadable}"
    ) {

  override protected def additionalDetails: Map[String, String] =
    Map(
      "workflowApiVersion"  -> workflowApiVersion.humanReadable,
      "supportedApiVersion" -> supportedApiVersion.humanReadable
    )

}
