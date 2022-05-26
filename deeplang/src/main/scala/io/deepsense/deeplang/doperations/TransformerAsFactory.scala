package io.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.deeplang._
import io.deepsense.deeplang.doperables.Transformer
import io.deepsense.deeplang.doperables.Estimator
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.inference.InferenceWarnings
import io.deepsense.deeplang.params.Param

abstract class TransformerAsFactory[T <: Transformer](implicit typeTag: TypeTag[T]) extends DOperation0To1[T] {

  val transformer: T = TypeUtils.instanceOfType(typeTag)

  override lazy val tTagTO_0: TypeTag[T] = typeTag[T]

  override val params: Array[Param[_]] = transformer.params

  setDefault(transformer.extractParamMap().toSeq: _*)

  override protected def execute()(context: ExecutionContext): T =
    updatedTransformer

  override def inferKnowledge()(context: InferContext): (DKnowledge[T], InferenceWarnings) =
    (DKnowledge[T](updatedTransformer), InferenceWarnings.empty)

  private def updatedTransformer: T = transformer.set(extractParamMap())

}
