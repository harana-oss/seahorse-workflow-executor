package io.deepsense.reportlib.model

import org.apache.spark.sql.types.{StructField, StructType}
import spray.json._

trait StructTypeJsonProtocol
  extends DefaultJsonProtocol
  with StructFieldJsonProtocol {

  val structTypeConstructor: (Array[StructField] => StructType) = StructType.apply
  implicit val structTypeFormat = jsonFormat(structTypeConstructor, "fields")
}
