package ai.deepsense.deeplang.actions

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.ActionObject
import ai.deepsense.deeplang.Action
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.Action._
import ai.deepsense.deeplang.actions.exceptions.UnknownOperationExecutionException
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings

import scala.reflect.runtime.{universe => ru}

class UnknownOperation extends Action {

  override val inArity = 0

  override val outArity = 0

  @transient
  override lazy val inPortTypes: Vector[ru.TypeTag[_]] = Vector()

  @transient
  override lazy val outPortTypes: Vector[ru.TypeTag[_]] = Vector()

  override val id: Id = "08752b37-3f90-4b8d-8555-e911e2de5662"

  override val name: String = "Unknown Operation"

  override val description: String = "Operation that could not be recognized by Seahorse"

  override val specificParams: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

  override def executeUntyped(arguments: Vector[ActionObject])(context: ExecutionContext): Vector[ActionObject] =
    throw new UnknownOperationExecutionException

  override def inferKnowledgeUntyped(knowledge: Vector[Knowledge[ActionObject]])(
      context: InferContext
  ): (Vector[Knowledge[ActionObject]], InferenceWarnings) =
    throw new UnknownOperationExecutionException

}
