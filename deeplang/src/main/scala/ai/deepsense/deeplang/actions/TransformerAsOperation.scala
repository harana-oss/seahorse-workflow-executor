package ai.deepsense.deeplang.actions

import scala.reflect.runtime.universe.TypeTag

import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.Transformer
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.Action1To2
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.utils.TypeUtils

abstract class TransformerAsOperation[T <: Transformer]()(implicit tag: TypeTag[T])
    extends Action1To2[DataFrame, DataFrame, T] {

  val transformer: T = TypeUtils.instanceOfType(tag)

  val specificParams = transformer.params

  setDefault(transformer.extractParamMap().toSeq: _*)

  override protected def execute(t0: DataFrame)(context: ExecutionContext): (DataFrame, T) = {
    transformer.set(extractParamMap())
    (transformer.transform(context)(())(t0), transformer)
  }

  override protected def inferKnowledge(
      dfKnowledge: Knowledge[DataFrame]
  )(ctx: InferContext): ((Knowledge[DataFrame], Knowledge[T]), InferenceWarnings) = {

    transformer.set(extractParamMap())
    val (outputDfKnowledge, warnings) = transformer.transform.infer(ctx)(())(dfKnowledge)
    ((outputDfKnowledge, Knowledge(transformer)), warnings)
  }

  override lazy val tTagTI_0: TypeTag[DataFrame] = typeTag

  override lazy val tTagTO_0: TypeTag[DataFrame] = typeTag

}
