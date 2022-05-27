package ai.deepsense.models.json.workflow

import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import spray.json.JsString
import spray.json.JsValue
import spray.json.JsonFormat

import ai.deepsense.deeplang.exceptions.FlowException

trait InferenceErrorJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {

  implicit object InferenceErrorMappingFormat extends JsonFormat[FlowException] {

    override def write(exc: FlowException): JsValue = JsString(exc.message)

    override def read(value: JsValue): FlowException =
      new FlowException(value.asInstanceOf[JsString].value)

  }

}
