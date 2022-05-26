package io.deepsense.models.json.workflow

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import spray.json._

import io.deepsense.commons.utils.Logging
import io.deepsense.commons.utils.Version
import io.deepsense.models.json.workflow.exceptions._
import io.deepsense.models.workflows._

trait WorkflowVersionUtil extends WorkflowWithResultsJsonProtocol with WorkflowWithVariablesJsonProtocol {

  this: Logging =>

  def currentVersion: Version

  def extractVersion(workflow: String): Try[Version] = Try {
    val workflowJson = workflow.parseJson.asJsObject
    extractVersion(workflowJson).map(Version(_)).get
  }

  def extractVersion(json: JsValue): Try[String] = Try {
    json.asJsObject
      .fields("metadata")
      .asJsObject
      .fields("apiVersion")
      .convertTo[String]
  }

  val versionedWorkflowReader = new VersionedJsonReader[Workflow]

  val versionedWorkflowWithResultsReader = new VersionedJsonReader[WorkflowWithResults]

  val versionedWorkflowWithVariablesReader = new VersionedJsonReader[WorkflowWithVariables]

  def workflowOrString(stringJson: String): Either[String, Workflow] =
    parsedOrString(versionedWorkflowReader, stringJson)

  private def parsedOrString[T](reader: JsonReader[T], stringJson: String): Either[String, T] =
    Try {
      Right(stringJson.parseJson.convertTo[T](reader))
    }.recover { case e: WorkflowVersionException =>
      Left(stringJson)
    }.get

  class VersionedJsonReader[T: JsonReader] extends RootJsonReader[T] {

    override def read(json: JsValue): T =
      whenVersionCurrent(json)(_.convertTo[T])

    def whenVersionCurrent(json: JsValue)(f: (JsValue) => T): T = {
      val versionString = extractVersion(json) match {
        case Failure(exception) =>
          throw WorkflowVersionNotFoundException(currentVersion)
        case Success(value) => value
      }

      Try(Version(versionString)) match {
        case Failure(exception) =>
          throw WorkflowVersionFormatException(versionString)
        case Success(parsedVersion) if parsedVersion.compatibleWith(currentVersion) =>
          f(json)
        case Success(parsedVersion) if !parsedVersion.compatibleWith(currentVersion) =>
          throw WorkflowVersionNotSupportedException(parsedVersion, currentVersion)
      }
    }

  }

}
