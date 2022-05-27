package ai.deepsense.deeplang.actions

import scala.reflect.runtime.universe.TypeTag

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action._
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.REvaluator

class CreateREvaluator extends EvaluatorAsFactory[REvaluator] with OperationDocumentation {

  override val id: Id = "1c626513-f266-4458-8499-29cbad95bb8c"

  override val name: String = "R Evaluator"

  override val description: String = "Creates an R Evaluator"

  override lazy val tTagTO_0: TypeTag[REvaluator] = typeTag

  override val since: Version = Version(1, 3, 0)

}
