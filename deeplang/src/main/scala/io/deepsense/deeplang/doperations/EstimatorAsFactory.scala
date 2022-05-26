package io.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.deeplang._
import io.deepsense.deeplang.doperables.Estimator
import io.deepsense.deeplang.doperables.Transformer
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.inference.InferenceWarnings
import io.deepsense.deeplang.params.Param

abstract class EstimatorAsFactory[E <: Estimator[Transformer]](implicit typeTagE: TypeTag[E])
    extends DOperation0To1[E] {

  val estimator: E = TypeUtils.instanceOfType(typeTagE)

  override lazy val tTagTO_0: TypeTag[E] = typeTag[E]

  override val params: Array[Param[_]] = estimator.params

  setDefault(estimator.extractParamMap().toSeq: _*)

  override protected def execute()(context: ExecutionContext): E =
    updatedEstimator

  override def inferKnowledge()(context: InferContext): (DKnowledge[E], InferenceWarnings) =
    (DKnowledge[E](updatedEstimator), InferenceWarnings.empty)

  private def updatedEstimator: E = estimator.set(extractParamMap())

}
