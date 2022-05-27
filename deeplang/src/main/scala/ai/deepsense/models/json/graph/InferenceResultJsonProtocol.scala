package ai.deepsense.models.json.graph

import spray.json._

import ai.deepsense.commons.json.envelope.Envelope
import ai.deepsense.commons.json.envelope.EnvelopeJsonFormat
import ai.deepsense.deeplang.actionobjects.descriptions.DataFrameInferenceResult
import ai.deepsense.deeplang.actionobjects.descriptions.InferenceResult
import ai.deepsense.deeplang.actionobjects.descriptions.ParamsInferenceResult
import ai.deepsense.reportlib.model._

trait InferenceResultJsonProtocol extends DefaultJsonProtocol with StructTypeJsonProtocol {

  implicit object InferenceResultWriter extends RootJsonWriter[InferenceResult] {

    implicit val dataFrameInferenceResultFormat = jsonFormat1(DataFrameInferenceResult)

    implicit val paramsInferenceResultFormat = {
      implicit val baseFormat = jsonFormat2(ParamsInferenceResult)
      EnvelopeJsonFormat[ParamsInferenceResult]("params")
    }

    override def write(obj: InferenceResult): JsValue = {
      obj match {
        case d: DataFrameInferenceResult => d.toJson
        case p: ParamsInferenceResult    => Envelope(p).toJson
      }
    }

  }

}

object InferenceResultJsonProtocol extends InferenceResultJsonProtocol
