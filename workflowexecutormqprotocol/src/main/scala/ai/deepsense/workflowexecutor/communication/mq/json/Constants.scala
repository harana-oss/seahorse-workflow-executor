package ai.deepsense.workflowexecutor.communication.mq.json

object Constants {

  object JsonKeys {

    val messageTypeKey = "messageType"

    val messageBodyKey = "messageBody"

  }

  object MessagesTypes {

    val heartbeat = "heartbeat"

    val poisonPill = "poisonPill"

    val ready = "ready"

    val launch = "launch"

    val inferredState = "inferredState"

    val executionReport = "executionStatus"

  }

}
