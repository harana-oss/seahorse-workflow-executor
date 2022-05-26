package ai.deepsense.deeplang.params.datasource

import java.util.UUID

import ai.deepsense.commons.json.UUIDJsonProtocol._
import ai.deepsense.deeplang.params.ParameterType.ParameterType
import ai.deepsense.deeplang.params.ParamWithJsFormat
import ai.deepsense.deeplang.params.ParameterType

case class DatasourceIdForReadParam(override val name: String, override val description: Option[String])
    extends ParamWithJsFormat[UUID] {

  override def replicate(name: String): DatasourceIdForReadParam = copy(name = name)

  override val parameterType: ParameterType = ParameterType.DatasourceIdForRead

}
