package ai.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import ai.deepsense.deeplang.doperables.Estimator
import ai.deepsense.deeplang.doperables.Transformer
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.DKnowledge
import ai.deepsense.deeplang.DOperation1To2
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.TypeUtils

abstract class EstimatorAsOperation[E <: Estimator[T], T <: Transformer]()(implicit
    typeTagE: TypeTag[E],
    typeTagT: TypeTag[T]
) extends DOperation1To2[DataFrame, DataFrame, T] {

  val estimator: E = TypeUtils.instanceOfType(typeTagE)

  val specificParams = estimator.params

  setDefault(estimator.extractParamMap().toSeq: _*)

  override protected def execute(t0: DataFrame)(context: ExecutionContext): (DataFrame, T) = {
    val transformer          = estimatorWithParams().fit(context)(())(t0)
    val transformedDataFrame = transformer.transform(context)(())(t0)
    (transformedDataFrame, transformer)
  }

  override protected def inferKnowledge(
      k0: DKnowledge[DataFrame]
  )(context: InferContext): ((DKnowledge[DataFrame], DKnowledge[T]), InferenceWarnings) = {

    val (transformerKnowledge, fitWarnings)     = estimatorWithParams().fit.infer(context)(())(k0)
    val (dataFrameKnowledge, transformWarnings) =
      transformerKnowledge.single.transform.infer(context)(())(k0)
    val warnings                                = fitWarnings ++ transformWarnings
    ((dataFrameKnowledge, transformerKnowledge), warnings)
  }

  private def estimatorWithParams() = {
    val estimatorWithParams = estimator.set(extractParamMap())
    validateDynamicParams(estimatorWithParams)
    estimatorWithParams
  }

  override lazy val tTagTI_0: TypeTag[DataFrame] = typeTag[DataFrame]

  override lazy val tTagTO_0: TypeTag[DataFrame] = typeTag[DataFrame]

  override lazy val tTagTO_1: TypeTag[T] = typeTag[T]

}
