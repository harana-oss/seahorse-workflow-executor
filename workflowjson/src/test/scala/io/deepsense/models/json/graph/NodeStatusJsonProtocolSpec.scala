package io.deepsense.models.json.graph

import spray.json._

import io.deepsense.commons.datetime.DateTimeConverter
import io.deepsense.commons.exception.DeepSenseFailure
import io.deepsense.commons.exception.FailureCode
import io.deepsense.commons.exception.FailureDescription
import io.deepsense.commons.models.Entity
import io.deepsense.graph.nodestate._

class NodeStatusJsonProtocolSpec extends GraphJsonTestSupport {

  import io.deepsense.commons.json.DateTimeJsonProtocol._
  import io.deepsense.models.json.graph.NodeStatusJsonProtocol._

  "NodeStateJsonProtocol" should {
    "transform Draft to Json" in {
      toJs(Draft(results)) shouldBe draftJson
    }
    "read Draft from Json" in {
      fromJs(draftJson) shouldBe Draft(results)
    }
    "transform Queued to Json" in {
      toJs(Queued(results)).toJson shouldBe queuedJson
    }
    "read Queued from Json" in {
      fromJs(queuedJson) shouldBe Queued(results)
    }
    "transform Running to Json" in {
      toJs(running) shouldBe
        runningJson
    }
    "read Running from Json" in {
      fromJs(runningJson) shouldBe running
    }
    "transform Completed to Json" in {
      toJs(completed) shouldBe completedJson
    }
    "read Completed from Json" in {
      fromJs(completedJson) shouldBe completed
    }
    "transform Failed to Json" in {
      toJs(failed) shouldBe failedJson
    }
    "read Failed from Json" in {
      fromJs(failedJson) shouldBe failed
    }
    "transform Aborted to Json" in {
      toJs(Aborted(results)) shouldBe abortedJson
    }
    "read Aborted from Json" in {
      fromJs(abortedJson) shouldBe Aborted(results)
    }
  }

  def fromJs(queuedJson: JsObject): NodeStatus =
    queuedJson.convertTo[NodeStatus]

  def toJs(state: NodeStatus): JsValue = state.toJson

  def js(state: String, fields: (String, JsValue)*): JsObject = {
    val emptyMap = Seq(
      NodeStatusJsonProtocol.Status,
      NodeStatusJsonProtocol.Started,
      NodeStatusJsonProtocol.Ended,
      NodeStatusJsonProtocol.Results,
      NodeStatusJsonProtocol.Error
    ).map(key => key -> None).toMap[String, Option[JsValue]]

    val jsFields = (emptyMap ++ fields.toMap.mapValues(Some(_)) +
      (NodeStatusJsonProtocol.Status -> Some(JsString(state)))).mapValues {
      case None    => JsNull
      case Some(v) => v
    }
    JsObject(jsFields)
  }

  val started = DateTimeConverter.now

  val ended = started.plusDays(1)

  val error = FailureDescription(
    DeepSenseFailure.Id.randomId,
    FailureCode.CannotUpdateRunningWorkflow,
    "This is a test FailureDescription",
    Some("This is a long test description"),
    Map("detail1" -> "value1", "detail2" -> "value2")
  )

  val results = Seq(Entity.Id.randomId, Entity.Id.randomId, Entity.Id.randomId)

  val failed = Failed(started, ended, error)

  val completed = Completed(started, ended, results)

  val running: Running = Running(started, results)

  val failedJson: JsObject = js("FAILED", "started" -> started.toJson, "ended" -> ended.toJson, "error" -> error.toJson)

  val completedJson: JsObject =
    js("COMPLETED", "started" -> started.toJson, "ended" -> ended.toJson, "results" -> results.toJson)

  val runningJson: JsObject =
    js("RUNNING", "started" -> started.toJson, "results" -> results.toJson)

  val abortedJson: JsObject = js("ABORTED", "results" -> results.toJson)

  val queuedJson: JsObject = js("QUEUED", "results" -> results.toJson)

  val draftJson: JsObject = js("DRAFT", "results" -> results.toJson)

}
