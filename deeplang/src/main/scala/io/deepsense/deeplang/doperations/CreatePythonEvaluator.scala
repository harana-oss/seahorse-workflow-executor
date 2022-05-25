package io.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.PythonEvaluator

class CreatePythonEvaluator
  extends EvaluatorAsFactory[PythonEvaluator] with OperationDocumentation  {

  override val id: Id = "582748ff-b1e4-4821-94da-d6c411e76e7e"
  override val name: String = "Python Evaluator"
  override val description: String =
    "Creates a Python Evaluator"

  override lazy val tTagTO_0: TypeTag[PythonEvaluator] = typeTag

  override val since: Version = Version(1, 2, 0)
}
