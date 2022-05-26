package io.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.deeplang._
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.Evaluator
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.inference.InferenceWarnings
import io.deepsense.deeplang.params.Param

abstract class EvaluatorAsFactory[T <: Evaluator](implicit typeTag: TypeTag[T]) extends DOperation0To1[T] {

  val evaluator: T = TypeUtils.instanceOfType(typeTag)

  override lazy val tTagTO_0: TypeTag[T] = typeTag[T]

  override val params: Array[Param[_]] = evaluator.params

  setDefault(evaluator.extractParamMap().toSeq: _*)

  override protected def execute()(context: ExecutionContext): T =
    updatedEvaluator

  override def inferKnowledge()(context: InferContext): (DKnowledge[T], InferenceWarnings) =
    (DKnowledge[T](updatedEvaluator), InferenceWarnings.empty)

  private def updatedEvaluator: T = evaluator.set(extractParamMap())

}
