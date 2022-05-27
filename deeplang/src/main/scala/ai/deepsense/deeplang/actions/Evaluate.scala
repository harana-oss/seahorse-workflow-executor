package ai.deepsense.deeplang.actions

import scala.reflect.runtime.universe.TypeTag

import spray.json.JsNull
import spray.json.JsValue

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.Evaluator
import ai.deepsense.deeplang.actionobjects.MetricValue
import ai.deepsense.deeplang.actions.exceptions.TooManyPossibleTypesException
import ai.deepsense.deeplang.actions.layout.SmallBlockLayout2To1
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.DynamicParameter
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.Action2To1
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

case class Evaluate()
    extends Action2To1[Evaluator, DataFrame, MetricValue]
    with SmallBlockLayout2To1
    with OperationDocumentation {

  override val id: Id = "a88eaf35-9061-4714-b042-ddd2049ce917"

  override val name: String = "Evaluate"

  override val description: String = "Evaluates a DataFrame using an Evaluator"

  override val since: Version = Version(1, 0, 0)

  val evaluatorParams = new DynamicParameter(
    name = "Parameters of input Evaluator",
    description = Some("These parameters are rendered dynamically, depending on type of Evaluator."),
    inputPort = 0
  )

  setDefault(evaluatorParams, JsNull)

  def getEvaluatorParams: JsValue = $(evaluatorParams)

  def setEvaluatorParams(jsValue: JsValue): this.type = set(evaluatorParams, jsValue)

  override val specificParams: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array(evaluatorParams)

  override lazy val tTagTI_0: TypeTag[Evaluator] = typeTag

  override lazy val tTagTI_1: TypeTag[DataFrame] = typeTag

  override lazy val tTagTO_0: TypeTag[MetricValue] = typeTag

  override protected def execute(evaluator: Evaluator, dataFrame: DataFrame)(context: ExecutionContext): MetricValue =
    evaluatorWithParams(evaluator, context.inferContext.graphReader).evaluate(context)(())(dataFrame)

  override protected def inferKnowledge(
                                         evaluatorKnowledge: Knowledge[Evaluator],
                                         dataFrameKnowledge: Knowledge[DataFrame]
  )(context: InferContext): (Knowledge[MetricValue], InferenceWarnings) = {

    if (evaluatorKnowledge.size > 1)
      throw TooManyPossibleTypesException()
    val evaluator = evaluatorKnowledge.single
    evaluatorWithParams(evaluator, context.graphReader).evaluate.infer(context)(())(dataFrameKnowledge)
  }

  private def evaluatorWithParams(evaluator: Evaluator, graphReader: GraphReader): Evaluator = {
    val evaluatorWithParams = evaluator
      .replicate()
      .setParamsFromJson(getEvaluatorParams, graphReader, ignoreNulls = true)
    validateDynamicParams(evaluatorWithParams)
    evaluatorWithParams
  }

}
