package ai.deepsense.deeplang

import spray.json.JsObject

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.parameters.custom.InnerWorkflow

trait InnerWorkflowExecutor {

  /** Parses inner workflow.
   *
   * @param workflow
   *   JSON containing workflow representation.
   * @return
   *   inner workflow as object.
   */
  def parse(workflow: JsObject): InnerWorkflow

  /** Serializes inner workflow to json.
   *
   * @param innerWorkflow
   *   to serialize.
   * @return
   *   json representation of the inner workflow.
   */
  def toJson(innerWorkflow: InnerWorkflow): JsObject

  /** Executes inner workflow.
    *
    * @param executionContext
    *   execution context.
    * @param workflow
    *   workflow to execute.
    * @param dataFrame
    *   input DataFrame for source node.
    * @return
    *   output DataFrame of sink node.
    */
  def execute(executionContext: CommonExecutionContext, workflow: InnerWorkflow, dataFrame: DataFrame): DataFrame

}
