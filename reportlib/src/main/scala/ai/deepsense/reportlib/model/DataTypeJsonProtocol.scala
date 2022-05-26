package ai.deepsense.reportlib.model

import org.apache.spark.sql.types.DataType
import spray.json._

trait DataTypeJsonProtocol {

  implicit val dataTypeFormat = new RootJsonFormat[DataType] {

    override def write(obj: DataType): JsValue = obj.json.parseJson

    override def read(json: JsValue): DataType = DataType.fromJson(json.compactPrint)

  }

}

object DataTypeJsonProtocol extends DataTypeJsonProtocol
