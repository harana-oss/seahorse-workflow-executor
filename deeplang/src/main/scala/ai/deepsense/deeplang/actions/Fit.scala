package ai.deepsense.deeplang.actions

import scala.reflect.runtime.universe.TypeTag

import spray.json.JsNull
import spray.json.JsValue

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.Estimator
import ai.deepsense.deeplang.actionobjects.Transformer
import ai.deepsense.deeplang.actions.exceptions.TooManyPossibleTypesException
import ai.deepsense.deeplang.actions.layout.SmallBlockLayout2To1
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.DynamicParameter
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.Action2To1
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

case class Fit()
    extends Action2To1[Estimator[Transformer], DataFrame, Transformer]
    with SmallBlockLayout2To1
    with OperationDocumentation {

  override val id: Id = "0c2ff818-977b-11e5-8994-feff819cdc9f"

  override val name: String = "Fit"

  override val description: String =
    "Fits an Estimator on a DataFrame"

  override val since: Version = Version(1, 0, 0)

  val estimatorParams = new DynamicParameter(
    name = "Parameters of input Estimator",
    description = Some("These parameters are rendered dynamically, depending on type of Estimator."),
    inputPort = 0
  )

  setDefault(estimatorParams -> JsNull)

  def setEstimatorParams(jsValue: JsValue): this.type = set(estimatorParams -> jsValue)

  override val specificParams: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array(estimatorParams)

  override lazy val tTagTI_0: TypeTag[Estimator[Transformer]] = typeTag

  override lazy val tTagTI_1: TypeTag[DataFrame] = typeTag

  override lazy val tTagTO_0: TypeTag[Transformer] = typeTag

  override protected def execute(estimator: Estimator[Transformer], dataFrame: DataFrame)(
      ctx: ExecutionContext
  ): Transformer =
    estimatorWithParams(estimator, ctx.inferContext.graphReader).fit(ctx)(())(dataFrame)

  override protected def inferKnowledge(
                                         estimatorKnowledge: Knowledge[Estimator[Transformer]],
                                         dataFrameKnowledge: Knowledge[DataFrame]
  )(ctx: InferContext): (Knowledge[Transformer], InferenceWarnings) = {

    if (estimatorKnowledge.size > 1)
      throw TooManyPossibleTypesException()
    val estimator = estimatorKnowledge.single
    estimatorWithParams(estimator, ctx.graphReader).fit.infer(ctx)(())(dataFrameKnowledge)
  }

  /** Note that Action should never mutate input ActionObject. This method copies input estimator and sets parameters
    * in copy.
    */
  private def estimatorWithParams(
      estimator: Estimator[Transformer],
      graphReader: GraphReader
  ): Estimator[Transformer] = {
    val estimatorWithParams = estimator
      .replicate()
      .setParamsFromJson($(estimatorParams), graphReader, ignoreNulls = true)
    validateDynamicParams(estimatorWithParams)
    estimatorWithParams
  }

}
