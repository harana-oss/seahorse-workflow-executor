package ai.deepsense.deeplang.parameters.datasource

import java.util.UUID

import ai.deepsense.commons.json.UUIDJsonProtocol._
import ai.deepsense.deeplang.parameters.ParameterType.ParameterType
import ai.deepsense.deeplang.parameters.ParameterWithJsFormat
import ai.deepsense.deeplang.parameters.ParameterType

case class DatasourceIdForReadParameter(override val name: String, override val description: Option[String])
    extends ParameterWithJsFormat[UUID] {

  override def replicate(name: String): DatasourceIdForReadParameter = copy(name = name)

  override val parameterType: ParameterType = ParameterType.DatasourceIdForRead

}
