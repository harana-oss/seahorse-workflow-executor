package io.deepsense.models.json.workflow

import spray.json._

import io.deepsense.graph.{GraphKnowledge, Node, NodeInferenceResult}
import io.deepsense.models.json.graph.DKnowledgeJsonProtocol
import io.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import io.deepsense.models.workflows.{ExecutionReport, InferredState}

trait InferredStateJsonProtocol extends WorkflowJsonProtocol
  with DKnowledgeJsonProtocol
  with ExecutionReportJsonProtocol
  with InferenceWarningsJsonProtocol {

  import InferredStateJsonProtocol._

  implicit val nodeInferenceResultFormat = jsonFormat3(NodeInferenceResult.apply)

  implicit val inferredStateWriter: RootJsonWriter[InferredState] =
    new RootJsonWriter[InferredState] {
      override def write(inferredState: InferredState): JsValue = {
        JsObject(
          idFieldName -> inferredState.id.toJson,
          knowledgeFieldName -> inferredState.graphKnowledge.results.toJson,
          statesFieldName -> inferredState.states.toJson)
      }
    }
  implicit val inferredStateReader: RootJsonReader[InferredState] =
    new RootJsonReader[InferredState] {
      override def read(json: JsValue): InferredState = {
        val fields = json.asJsObject.fields
        val inferenceResults = fields(knowledgeFieldName).convertTo[Map[Node.Id, NodeInferenceResult]]
        InferredState(
          fields(idFieldName).convertTo[Node.Id],
          GraphKnowledge(inferenceResults),
          fields(statesFieldName).convertTo[ExecutionReport]
        )
      }
    }
}

object InferredStateJsonProtocol {

  def apply(_graphReader: GraphReader): InferredStateJsonProtocol = new InferredStateJsonProtocol {
    override val graphReader = _graphReader
  }

  val idFieldName = "id"
  val knowledgeFieldName = "knowledge"
  val statesFieldName = "states"
}
