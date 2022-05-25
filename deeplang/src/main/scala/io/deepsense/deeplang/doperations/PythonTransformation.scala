package io.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.PythonTransformer

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
