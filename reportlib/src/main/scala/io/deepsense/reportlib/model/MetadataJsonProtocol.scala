package io.deepsense.reportlib.model

import org.apache.spark.sql.types.Metadata
import spray.json._

trait MetadataJsonProtocol {
  implicit val metadataFormat = new RootJsonFormat[Metadata] {
    override def write(obj: Metadata): JsValue = obj.json.parseJson
    override def read(json: JsValue): Metadata = Metadata.fromJson(json.compactPrint)
  }
}

object MetadataJsonProtocol extends MetadataJsonProtocol
