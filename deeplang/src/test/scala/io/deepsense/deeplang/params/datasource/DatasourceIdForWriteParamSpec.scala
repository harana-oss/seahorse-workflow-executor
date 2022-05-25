package io.deepsense.deeplang.params.datasource

import java.util.UUID

import spray.json._
import io.deepsense.deeplang.params.AbstractParamSpec

class DatasourceIdForWriteParamSpec extends AbstractParamSpec[UUID, DatasourceIdForWriteParam] {

  override def className: String = "DatasourceIdForWriteParam"

  override def paramFixture: (DatasourceIdForWriteParam, JsValue) = {
    val param = DatasourceIdForWriteParam(
      name = "Ds for write parameter name",
      description = None)
    val expectedJson = JsObject(
      "type" -> JsString("datasourceIdForWrite"),
      "name" -> JsString(param.name),
      "description" -> JsString(""),
      "default" -> JsNull,
      "isGriddable" -> JsFalse
    )
    (param, expectedJson)
  }

  override def valueFixture: (UUID, JsValue) = {
    val value = UUID.randomUUID()
    (value, JsString(value.toString))
  }
}
