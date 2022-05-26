package io.deepsense.workflowexecutor.communication.mq.json

import scala.reflect.ClassTag

import spray.json._

import Constants.JsonKeys._

class DefaultJsonMessageSerializer[T: JsonWriter: ClassTag](typeName: String) extends JsonMessageSerializer {

  val serialize: PartialFunction[Any, JsObject] = {
    case o if isHandled(o) => handle(o.asInstanceOf[T])
  }

  private def isHandled(obj: Any): Boolean = implicitly[ClassTag[T]].runtimeClass.isInstance(obj)

  private def handle(body: T): JsObject = JsObject(
    messageTypeKey -> JsString(typeName),
    messageBodyKey -> body.toJson
  )

}
