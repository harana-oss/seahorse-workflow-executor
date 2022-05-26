package io.deepsense.models.json.graph

import spray.json._

import io.deepsense.deeplang.doperables.descriptions.InferenceResult
import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.DKnowledge
import io.deepsense.deeplang.DOperable

trait DKnowledgeJsonProtocol extends DefaultJsonProtocol {

  implicit object DKnowledgeJsonFormat
      extends JsonFormat[DKnowledge[DOperable]]
      with InferenceResultJsonProtocol
      with DefaultJsonProtocol {

    override def write(dKnowledge: DKnowledge[DOperable]): JsValue = {
      val types  = typeArray(dKnowledge)
      val result = inferenceResult(dKnowledge)

      JsObject(
        "types"  -> types,
        "result" -> result.getOrElse(JsNull)
      )
    }

    def inferenceResult(dKnowledge: DKnowledge[DOperable]): Option[JsValue] =
      if (dKnowledge.size != 1)
        None
      else
        dKnowledge.single.inferenceResult
          .map(_.toJson)

    def typeArray(dKnowledge: DKnowledge[DOperable]): JsArray =
      JsArray(dKnowledge.types.map(_.getClass.getName.toJson).toVector)

    // FIXME: This isn't much better deserialization than the previous
    // FIXME: '???' placeholder, but at least it doesn't throw an exception.
    // FIXME: We should consider in a near future (today is 16.11.2016) to
    // FIXME: implement it properly or find some other means to not silently
    // FIXME: discard the information contained inside passed json value.
    override def read(json: JsValue): DKnowledge[DOperable] = DKnowledge()

  }

}

object DKnowledgeJsonProtocol extends DKnowledgeJsonProtocol
