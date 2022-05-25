package io.deepsense.models.json.graph

import spray.json._

import io.deepsense.commons.json.envelope.{Envelope, EnvelopeJsonFormat}
import io.deepsense.deeplang.doperables.descriptions.{DataFrameInferenceResult, InferenceResult, ParamsInferenceResult}
import io.deepsense.reportlib.model._

trait InferenceResultJsonProtocol
  extends DefaultJsonProtocol
  with StructTypeJsonProtocol {

  implicit object InferenceResultWriter extends RootJsonWriter[InferenceResult] {

    implicit val dataFrameInferenceResultFormat = jsonFormat1(DataFrameInferenceResult)

    implicit val paramsInferenceResultFormat = {
      implicit val baseFormat = jsonFormat2(ParamsInferenceResult)
      EnvelopeJsonFormat[ParamsInferenceResult]("params")
    }

    override def write(obj: InferenceResult): JsValue = {
      obj match {
        case d: DataFrameInferenceResult => d.toJson
        case p: ParamsInferenceResult => Envelope(p).toJson
      }
    }
  }
}

object InferenceResultJsonProtocol extends InferenceResultJsonProtocol

