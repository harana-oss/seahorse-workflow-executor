package io.deepsense.models.json.workflow

import spray.httpx.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsString, JsValue, JsonFormat}

import io.deepsense.deeplang.exceptions.DeepLangException

trait InferenceErrorJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {
  implicit object InferenceErrorMappingFormat extends JsonFormat[DeepLangException] {
    override def write(exc: DeepLangException): JsValue = JsString(exc.message)
    override def read(value: JsValue): DeepLangException =
      new DeepLangException(value.asInstanceOf[JsString].value)
  }
}
