package ai.deepsense.deeplang.actions

import scala.reflect.runtime.universe.TypeTag

import spray.json.JsNull
import spray.json.JsValue

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.Transformer
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.layout.SmallBlockLayout2To1
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.DynamicParameter
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.Action2To1
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

case class Transform()
    extends Action2To1[Transformer, DataFrame, DataFrame]
    with SmallBlockLayout2To1
    with OperationDocumentation {

  override val id: Id = "643d8706-24db-4674-b5b4-10b5129251fc"

  override val name: String = "Transform"

  override val description: String =
    "Transforms a DataFrame using a Transformer"

  override val since: Version = Version(1, 0, 0)

  val transformerParams = new DynamicParameter(
    name = "Parameters of input Transformer",
    description = Some("These parameters are rendered dynamically, depending on type of Transformer."),
    inputPort = 0
  )

  setDefault(transformerParams, JsNull)

  def getTransformerParams: JsValue = $(transformerParams)

  def setTransformerParams(jsValue: JsValue): this.type = set(transformerParams, jsValue)

  override val specificParams: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array(transformerParams)

  override lazy val tTagTI_0: TypeTag[Transformer] = typeTag

  override lazy val tTagTI_1: TypeTag[DataFrame] = typeTag

  override lazy val tTagTO_0: TypeTag[DataFrame] = typeTag

  override protected def execute(transformer: Transformer, dataFrame: DataFrame)(context: ExecutionContext): DataFrame =
    transformerWithParams(transformer, context.inferContext.graphReader).transform(context)(())(dataFrame)

  override protected def inferKnowledge(
                                         transformerKnowledge: Knowledge[Transformer],
                                         dataFrameKnowledge: Knowledge[DataFrame]
  )(context: InferContext): (Knowledge[DataFrame], InferenceWarnings) = {

    if (transformerKnowledge.size > 1)
      (Knowledge(DataFrame.forInference()), InferenceWarnings.empty)
    else {
      val transformer = transformerKnowledge.single
      transformerWithParams(transformer, context.graphReader).transform.infer(context)(())(dataFrameKnowledge)
    }
  }

  private def transformerWithParams(transformer: Transformer, graphReader: GraphReader): Transformer = {
    val transformerWithParams = transformer
      .replicate()
      .setParamsFromJson(getTransformerParams, graphReader, ignoreNulls = true)
    validateDynamicParams(transformerWithParams)
    transformerWithParams
  }

}
