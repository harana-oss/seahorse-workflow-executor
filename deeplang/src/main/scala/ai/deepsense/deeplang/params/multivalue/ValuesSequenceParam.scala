package ai.deepsense.deeplang.params.multivalue

import spray.json._

case class ValuesSequenceParam[T](sequence: List[T]) extends MultipleValuesParam[T] {

  override def values(): List[T] = sequence

}

object ValuesSequenceParam {

  val paramType = "seq"

}

object ValuesSequenceParamJsonProtocol extends DefaultJsonProtocol {

  implicit def valuesSequenceParamFormat[A: JsonFormat]: RootJsonFormat[ValuesSequenceParam[A]] =
    jsonFormat1(ValuesSequenceParam.apply[A])

}
