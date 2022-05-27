package ai.deepsense.deeplang.actions

import scala.reflect.runtime.universe.TypeTag
import ai.deepsense.deeplang._
import ai.deepsense.deeplang.actionobjects.Transformer
import ai.deepsense.deeplang.actionobjects.Estimator
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.utils.TypeUtils

abstract class TransformerAsFactory[T <: Transformer](implicit typeTag: TypeTag[T]) extends Action0To1[T] {

  val transformer: T = TypeUtils.instanceOfType(typeTag)

  override lazy val tTagTO_0: TypeTag[T] = typeTag[T]

  override val specificParams: Array[Parameter[_]] = transformer.params

  setDefault(transformer.extractParamMap().toSeq: _*)

  override protected def execute()(context: ExecutionContext): T =
    updatedTransformer

  override def inferKnowledge()(context: InferContext): (Knowledge[T], InferenceWarnings) =
    (Knowledge[T](updatedTransformer), InferenceWarnings.empty)

  private def updatedTransformer: T = transformer.set(extractParamMap())

}
