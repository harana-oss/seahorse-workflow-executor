package ai.deepsense.deeplang.parameters.datasource

import java.util.UUID

import spray.json._
import ai.deepsense.deeplang.parameters.AbstractParameterSpec

class DatasourceIdForWriteParamSpec extends AbstractParameterSpec[UUID, DatasourceIdForWriteParameter] {

  override def className: String = "DatasourceIdForWriteParam"

  override def paramFixture: (DatasourceIdForWriteParameter, JsValue) = {
    val param        = DatasourceIdForWriteParameter(name = "Ds for write parameter name", description = None)
    val expectedJson = JsObject(
      "type"        -> JsString("datasourceIdForWrite"),
      "name"        -> JsString(param.name),
      "description" -> JsString(""),
      "default"     -> JsNull,
      "isGriddable" -> JsFalse
    )
    (param, expectedJson)
  }

  override def valueFixture: (UUID, JsValue) = {
    val value = UUID.randomUUID()
    (value, JsString(value.toString))
  }

}
