package ai.deepsense.deeplang.actions

import scala.reflect.runtime.universe.TypeTag
import ai.deepsense.deeplang._
import ai.deepsense.deeplang.actionobjects.Estimator
import ai.deepsense.deeplang.actionobjects.Transformer
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.utils.TypeUtils

abstract class EstimatorAsFactory[E <: Estimator[Transformer]](implicit typeTagE: TypeTag[E])
    extends Action0To1[E] {

  val estimator: E = TypeUtils.instanceOfType(typeTagE)

  override lazy val tTagTO_0: TypeTag[E] = typeTag[E]

  override val specificParams: Array[Parameter[_]] = estimator.params

  setDefault(estimator.extractParamMap().toSeq: _*)

  override protected def execute()(context: ExecutionContext): E =
    updatedEstimator

  override def inferKnowledge()(context: InferContext): (Knowledge[E], InferenceWarnings) =
    (Knowledge[E](updatedEstimator), InferenceWarnings.empty)

  private def updatedEstimator: E = estimator.set(extractParamMap())

}
