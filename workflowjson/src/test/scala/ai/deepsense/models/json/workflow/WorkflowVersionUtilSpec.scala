package ai.deepsense.models.json.workflow

import scala.util.Success

import org.scalatest.matchers.should.Matchers

import org.mockito.Mockito._
import org.mockito.ArgumentMatchers.any
import spray.json._

import ai.deepsense.commons.utils.Logging
import ai.deepsense.commons.utils.Version
import ai.deepsense.graph.FlowGraph
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import ai.deepsense.models.json.workflow.exceptions.WorkflowVersionFormatException
import ai.deepsense.models.json.StandardSpec
import ai.deepsense.models.json.UnitTestSupport
import ai.deepsense.models.workflows._

class WorkflowVersionUtilSpec extends StandardSpec with UnitTestSupport with Logging with WorkflowVersionUtil {

  val currentVersionString = "1.2.3"

  override def currentVersion: Version = Version(currentVersionString)

  override val graphReader = mock[GraphReader]

  when(graphReader.read(any())).thenReturn(FlowGraph())

  "WorkflowVersionUtil" should {
    "allow to extract the version as a string and as an object" in {
      val versionString = "3.2.1"
      val okJson        = JsObject("metadata" -> JsObject("apiVersion" -> JsString(versionString)))
      extractVersion(okJson) shouldBe Success(versionString)
      extractVersion(okJson.compactPrint) shouldBe Success(Version(versionString))

      val wrongJson = JsObject("metadataFOO" -> JsObject("apiVersion" -> JsString(versionString)))
      extractVersion(wrongJson) shouldBe 'Failure
      extractVersion(wrongJson.compactPrint) shouldBe 'Failure
    }

    "parse a Workflow and return an object or a string if version is invalid" in {
      workflowOrString(correctWorkflow.toJson.compactPrint) shouldBe Right(correctWorkflow)
      workflowOrString(incorrectVersionJsonString) shouldBe Left(incorrectVersionJsonString)
    }

    "expose a JsonReader for Workflow that checks the version" in {
      correctWorkflowString.parseJson.convertTo[Workflow](versionedWorkflowReader) shouldBe
        correctWorkflow

      an[WorkflowVersionFormatException] shouldBe
        thrownBy(incorrectVersionJsonString.parseJson.convertTo[Workflow](versionedWorkflowReader))
    }

    "expose a JsonReader for WorkflowWithResults that checks the version" in {
      workflowWithResultsString.parseJson
        .convertTo[WorkflowWithResults](versionedWorkflowWithResultsReader) shouldBe
        workflowWithResults

      an[WorkflowVersionFormatException] shouldBe
        thrownBy(
          incorrectVersionJsonString.parseJson
            .convertTo[WorkflowWithResults](versionedWorkflowWithResultsReader)
        )
    }
  }

  val correctVersionMeta = WorkflowMetadata(WorkflowType.Batch, currentVersionString)

  val incorrectVersionMeta = correctVersionMeta.copy(apiVersion = "X" + currentVersionString)

  val correctWorkflow = Workflow(correctVersionMeta, FlowGraph(), JsObject())

  val correctWorkflowString = correctWorkflow.toJson.prettyPrint

  val incorrectVersionJson =
    JsObject("metadata" -> JsObject("apiVersion" -> JsString("FOOBAR")), "foo" -> JsString("bar"))

  val incorrectVersionJsonString = incorrectVersionJson.compactPrint

  val workflowId = Workflow.Id.randomId

  val workflowWithResults = WorkflowWithResults(
    workflowId,
    correctVersionMeta,
    FlowGraph(),
    JsObject(),
    ExecutionReport(Map(), EntitiesMap(), None),
    WorkflowInfo.forId(workflowId)
  )

  val workflowWithResultsString = workflowWithResults.toJson.compactPrint

}
