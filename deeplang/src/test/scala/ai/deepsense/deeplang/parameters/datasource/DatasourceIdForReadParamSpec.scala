package ai.deepsense.deeplang.parameters.datasource

import java.util.UUID

import spray.json._
import ai.deepsense.deeplang.parameters.AbstractParameterSpec

class DatasourceIdForReadParamSpec extends AbstractParameterSpec[UUID, DatasourceIdForReadParameter] {

  override def className: String = "DatasourceIdForReadParam"

  override def paramFixture: (DatasourceIdForReadParameter, JsValue) = {
    val param        = DatasourceIdForReadParameter(name = "Ds for read parameter name", description = None)
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
