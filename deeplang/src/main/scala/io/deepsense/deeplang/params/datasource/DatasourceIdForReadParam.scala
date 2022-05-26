package io.deepsense.deeplang.params.datasource

import java.util.UUID

import io.deepsense.commons.json.UUIDJsonProtocol._
import io.deepsense.deeplang.params.ParameterType.ParameterType
import io.deepsense.deeplang.params.ParamWithJsFormat
import io.deepsense.deeplang.params.ParameterType

case class DatasourceIdForReadParam(override val name: String, override val description: Option[String])
    extends ParamWithJsFormat[UUID] {

  override def replicate(name: String): DatasourceIdForReadParam = copy(name = name)

  override val parameterType: ParameterType = ParameterType.DatasourceIdForRead

}
