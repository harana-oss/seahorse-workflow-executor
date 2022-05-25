package io.deepsense.reportlib.model

import org.apache.spark.sql.types.{DataType, StructField}
import spray.json._

import io.deepsense.commons.json.EnumerationSerializer
import io.deepsense.commons.types.{ColumnType, SparkConversions}

trait StructFieldJsonProtocol
  extends DefaultJsonProtocol
  with MetadataJsonProtocol
  with DataTypeJsonProtocol {

  implicit val failureCodeFormat = EnumerationSerializer.jsonEnumFormat(ColumnType)

  // StructField format without metadata, with deeplangType appended
  implicit val structFieldFormat = new RootJsonFormat[StructField] {
    val c = (s: String, d: DataType, b: Boolean) => StructField(s, d, b)
    implicit val rawFormat = jsonFormat(c, "name", "dataType", "nullable")

    override def write(obj: StructField): JsValue = {
      val jsObject = obj.toJson(rawFormat).asJsObject

      val deeplangType =
        SparkConversions.sparkColumnTypeToColumnType(obj.dataType)

      JsObject(jsObject.fields + ("deeplangType" -> deeplangType.toJson))
    }

    override def read(json: JsValue): StructField = {
      json.convertTo(rawFormat)
    }
  }
}
