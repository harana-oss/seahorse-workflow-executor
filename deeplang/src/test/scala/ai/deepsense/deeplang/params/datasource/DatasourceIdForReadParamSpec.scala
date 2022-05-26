package ai.deepsense.deeplang.params.datasource

import java.util.UUID

import spray.json._
import ai.deepsense.deeplang.params.AbstractParamSpec

class DatasourceIdForReadParamSpec extends AbstractParamSpec[UUID, DatasourceIdForReadParam] {

  override def className: String = "DatasourceIdForReadParam"

  override def paramFixture: (DatasourceIdForReadParam, JsValue) = {
    val param        = DatasourceIdForReadParam(name = "Ds for read parameter name", description = None)
    val expectedJson = JsObject(
      "type"        -> JsString("datasourceIdForRead"),
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
