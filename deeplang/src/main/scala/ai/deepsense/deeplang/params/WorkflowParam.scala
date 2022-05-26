package ai.deepsense.deeplang.params

import spray.json._
import spray.json.DefaultJsonProtocol._

import ai.deepsense.deeplang.exceptions.DeepLangException
import ai.deepsense.deeplang.params.custom.InnerWorkflow
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import ai.deepsense.models.json.workflow.InnerWorkflowJsonReader
import ai.deepsense.models.json.workflow.WriteInnerWorkflowJsonProtocol

case class WorkflowParam(override val name: String, override val description: Option[String])
    extends Param[InnerWorkflow]
    with WriteInnerWorkflowJsonProtocol {

  override val parameterType = ParameterType.Workflow

  override def valueToJson(value: InnerWorkflow): JsValue = value.toJson

  override def valueFromJson(jsValue: JsValue, graphReader: GraphReader): InnerWorkflow =
    InnerWorkflowJsonReader.toInner(jsValue, graphReader)

  override def validate(value: InnerWorkflow): Vector[DeepLangException] =
    super.validate(value)

  override def replicate(name: String): WorkflowParam = copy(name = name)

}
