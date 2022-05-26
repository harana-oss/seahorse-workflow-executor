package ai.deepsense.deeplang

trait CustomCodeExecutor {

  /** Validates custom operation's source code.
    * @param code
    *   Code to be validated.
    * @return
    *   True if validation passed, False otherwise.
    */
  def isValid(code: String): Boolean

  /** Executes custom operation's source code
    * @param workflowId
    *   Id of the workflow.
    * @param nodeId
    *   Id of the node.
    * @param code
    *   Code to be executed.
    */
  def run(workflowId: String, nodeId: String, code: String): Unit

}
