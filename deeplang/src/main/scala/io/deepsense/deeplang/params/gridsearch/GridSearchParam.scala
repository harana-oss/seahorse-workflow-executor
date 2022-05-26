package io.deepsense.deeplang.params.gridsearch

import io.deepsense.deeplang.params.ParameterType
import io.deepsense.deeplang.params.DynamicParam
import io.deepsense.deeplang.params.ParameterType._

class GridSearchParam(override val name: String, override val description: Option[String], override val inputPort: Int)
    extends DynamicParam(name, description, inputPort) {

  override val parameterType: ParameterType = ParameterType.GridSearch

  override def replicate(name: String): GridSearchParam =
    new GridSearchParam(name, description, inputPort)

}
