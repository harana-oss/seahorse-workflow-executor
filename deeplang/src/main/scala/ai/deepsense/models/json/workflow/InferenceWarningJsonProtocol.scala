package ai.deepsense.models.json.workflow

import spray.httpx.SprayJsonSupport
import spray.json._

import ai.deepsense.deeplang.inference.InferenceWarning
import ai.deepsense.deeplang.inference.InferenceWarnings

trait InferenceWarningJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {

  implicit object InferenceWarningMappingFormat extends JsonFormat[InferenceWarning] {

    override def write(warning: InferenceWarning): JsValue = JsString(warning.message)

    override def read(value: JsValue): InferenceWarning =
      new InferenceWarning(value.asInstanceOf[JsString].value) {}

  }

}

trait InferenceWarningsJsonProtocol
    extends DefaultJsonProtocol
    with SprayJsonSupport
    with InferenceWarningJsonProtocol {

  implicit object InferenceWarningsMappingFormat extends JsonFormat[InferenceWarnings] {

    override def write(warnings: InferenceWarnings): JsValue = warnings.warnings.toJson

    override def read(value: JsValue): InferenceWarnings =
      InferenceWarnings(value.asInstanceOf[JsArray].convertTo[Vector[InferenceWarning]])

  }

}
