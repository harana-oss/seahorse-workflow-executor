package io.deepsense.deeplang.doperations

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.{DKnowledge, DOperable, DOperation, ExecutionContext}
import io.deepsense.deeplang.DOperation._
import io.deepsense.deeplang.doperations.exceptions.UnknownOperationExecutionException
import io.deepsense.deeplang.inference.{InferContext, InferenceWarnings}

import scala.reflect.runtime.{universe => ru}


class UnknownOperation extends DOperation {

  override val inArity = 0
  override val outArity = 0

  @transient
  override lazy val inPortTypes: Vector[ru.TypeTag[_]] = Vector()

  @transient
  override lazy val outPortTypes: Vector[ru.TypeTag[_]] = Vector()


  override val id: Id = "08752b37-3f90-4b8d-8555-e911e2de5662"
  override val name: String = "Unknown Operation"
  override val description: String = "Operation that could not be recognized by Seahorse"

  override val params: Array[io.deepsense.deeplang.params.Param[_]] = Array()

  override def executeUntyped(arguments: Vector[DOperable])(context: ExecutionContext): Vector[DOperable] = {
    throw new UnknownOperationExecutionException
  }

  override def inferKnowledgeUntyped(knowledge: Vector[DKnowledge[DOperable]])(
      context: InferContext): (Vector[DKnowledge[DOperable]], InferenceWarnings) = {
    throw new UnknownOperationExecutionException
  }
}
