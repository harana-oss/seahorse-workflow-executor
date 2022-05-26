package ai.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import ai.deepsense.deeplang._
import ai.deepsense.deeplang.doperables.Estimator
import ai.deepsense.deeplang.doperables.Transformer
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.params.Param

abstract class EstimatorAsFactory[E <: Estimator[Transformer]](implicit typeTagE: TypeTag[E])
    extends DOperation0To1[E] {

  val estimator: E = TypeUtils.instanceOfType(typeTagE)

  override lazy val tTagTO_0: TypeTag[E] = typeTag[E]

  override val specificParams: Array[Param[_]] = estimator.params

  setDefault(estimator.extractParamMap().toSeq: _*)

  override protected def execute()(context: ExecutionContext): E =
    updatedEstimator

  override def inferKnowledge()(context: InferContext): (DKnowledge[E], InferenceWarnings) =
    (DKnowledge[E](updatedEstimator), InferenceWarnings.empty)

  private def updatedEstimator: E = estimator.set(extractParamMap())

}
