package ai.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.doperables.Transformer
import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.DKnowledge
import ai.deepsense.deeplang.DOperation1To2
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.TypeUtils

abstract class TransformerAsOperation[T <: Transformer]()(implicit tag: TypeTag[T])
    extends DOperation1To2[DataFrame, DataFrame, T] {

  val transformer: T = TypeUtils.instanceOfType(tag)

  val specificParams = transformer.params

  setDefault(transformer.extractParamMap().toSeq: _*)

  override protected def execute(t0: DataFrame)(context: ExecutionContext): (DataFrame, T) = {
    transformer.set(extractParamMap())
    (transformer.transform(context)(())(t0), transformer)
  }

  override protected def inferKnowledge(
      dfKnowledge: DKnowledge[DataFrame]
  )(ctx: InferContext): ((DKnowledge[DataFrame], DKnowledge[T]), InferenceWarnings) = {

    transformer.set(extractParamMap())
    val (outputDfKnowledge, warnings) = transformer.transform.infer(ctx)(())(dfKnowledge)
    ((outputDfKnowledge, DKnowledge(transformer)), warnings)
  }

  override lazy val tTagTI_0: TypeTag[DataFrame] = typeTag

  override lazy val tTagTO_0: TypeTag[DataFrame] = typeTag

}
