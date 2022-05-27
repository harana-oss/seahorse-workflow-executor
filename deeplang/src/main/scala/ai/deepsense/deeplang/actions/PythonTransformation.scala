package ai.deepsense.deeplang.actions

import scala.reflect.runtime.universe.TypeTag

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.PythonTransformer

case class PythonTransformation() extends TransformerAsOperation[PythonTransformer] with OperationDocumentation {

  override val id: Id = "a721fe2a-5d7f-44b3-a1e7-aade16252ead"

  override val name: String = "Python Transformation"

  override val description: String = "Creates a custom Python transformation"

  override lazy val tTagTO_1: TypeTag[PythonTransformer] = typeTag

  override val since: Version = Version(1, 0, 0)

}

object PythonTransformation {

  val InputPortNumber: Int = 0

  val OutputPortNumber: Int = 0

}
