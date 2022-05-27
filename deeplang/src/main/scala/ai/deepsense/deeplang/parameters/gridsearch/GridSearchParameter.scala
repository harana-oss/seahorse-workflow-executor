package ai.deepsense.deeplang.parameters.gridsearch

import ai.deepsense.deeplang.parameters.ParameterType
import ai.deepsense.deeplang.parameters.DynamicParameter
import ai.deepsense.deeplang.parameters.ParameterType._

class GridSearchParameter(override val name: String, override val description: Option[String], override val inputPort: Int)
    extends DynamicParameter(name, description, inputPort) {

  override val parameterType: ParameterType = ParameterType.GridSearch

  override def replicate(name: String): GridSearchParameter =
    new GridSearchParameter(name, description, inputPort)

}
