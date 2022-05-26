package ai.deepsense.models.workflows

object WorkflowType extends Enumeration {

  type WorkflowType = Value

  val Batch = new Val("batch")

  val Streaming = new Val("streaming")

}
