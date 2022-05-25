package io.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.Transformer
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.inference.{InferContext, InferenceWarnings}
import io.deepsense.deeplang.{DKnowledge, DOperation1To2, ExecutionContext, TypeUtils}

abstract class TransformerAsOperation[T <: Transformer]
    ()(implicit tag: TypeTag[T])
  extends DOperation1To2[DataFrame, DataFrame, T] {

  val transformer: T = TypeUtils.instanceOfType(tag)

  val params = transformer.params

  setDefault(transformer.extractParamMap().toSeq: _*)

  override protected def execute(t0: DataFrame)(context: ExecutionContext): (DataFrame, T) = {
    transformer.set(extractParamMap())
    (transformer.transform(context)(())(t0), transformer)
  }

  override protected def inferKnowledge(dfKnowledge: DKnowledge[DataFrame])(ctx: InferContext)
    : ((DKnowledge[DataFrame], DKnowledge[T]), InferenceWarnings) = {

    transformer.set(extractParamMap())
    val (outputDfKnowledge, warnings) = transformer.transform.infer(ctx)(())(dfKnowledge)
    ((outputDfKnowledge, DKnowledge(transformer)), warnings)
  }

  override lazy val tTagTI_0: TypeTag[DataFrame] = typeTag
  override lazy val tTagTO_0: TypeTag[DataFrame] = typeTag
}
