package ai.deepsense.deeplang.actions

import scala.reflect.runtime.universe._

import spray.json.JsNull
import spray.json.JsValue

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.Estimator
import ai.deepsense.deeplang.actionobjects.Transformer
import ai.deepsense.deeplang.actions.exceptions.TooManyPossibleTypesException
import ai.deepsense.deeplang.actions.layout.SmallBlockLayout2To2
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.DynamicParameter
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.Action2To2
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

class FitPlusTransform
    extends Action2To2[Estimator[Transformer], DataFrame, DataFrame, Transformer]
    with SmallBlockLayout2To2
    with OperationDocumentation {

  override val id: Id = "1cb153f1-3731-4046-a29b-5ad64fde093f"

  override val name: String = "Fit + Transform"

  override val description: String = "Fits an Estimator on a DataFrame and transforms it"

  override val since: Version = Version(1, 0, 0)

  override lazy val tTagTI_0: TypeTag[Estimator[Transformer]] = typeTag[Estimator[Transformer]]

  override lazy val tTagTI_1: TypeTag[DataFrame] = typeTag[DataFrame]

  override lazy val tTagTO_0: TypeTag[DataFrame] = typeTag[DataFrame]

  override lazy val tTagTO_1: TypeTag[Transformer] = typeTag[Transformer]

  val estimatorParams = new DynamicParameter(
    name = "Parameters of input Estimator",
    description = Some("These parameters are rendered dynamically, depending on type of Estimator."),
    inputPort = 0
  )

  setDefault(estimatorParams -> JsNull)

  def setEstimatorParams(jsValue: JsValue): this.type = set(estimatorParams -> jsValue)

  override val specificParams: Array[Parameter[_]] = Array(estimatorParams)

  override protected def execute(estimator: Estimator[Transformer], dataFrame: DataFrame)(
      context: ExecutionContext
  ): (DataFrame, Transformer) = {
    val estimatorToRun           = estimatorWithParams(estimator, context.inferContext.graphReader)
    val transformer: Transformer = estimatorToRun.fit(context)(())(dataFrame)
    val transformed: DataFrame   = transformer.transform(context)(())(dataFrame)
    (transformed, transformer)
  }

  override protected def inferKnowledge(
                                         estimatorKnowledge: Knowledge[Estimator[Transformer]],
                                         inputDataFrameKnowledge: Knowledge[DataFrame]
  )(context: InferContext): ((Knowledge[DataFrame], Knowledge[Transformer]), InferenceWarnings) = {

    val (transformerKnowledge, transformerWarnings) =
      inferTransformer(context, estimatorKnowledge, inputDataFrameKnowledge)

    val (transformedDataFrameKnowledge, transformedDataFrameWarnings) =
      inferDataFrame(context, inputDataFrameKnowledge, transformerKnowledge)

    val warningsSum: InferenceWarnings = transformerWarnings ++ transformedDataFrameWarnings
    ((transformedDataFrameKnowledge, transformerKnowledge), warningsSum)
  }

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

  private def inferDataFrame(
                              context: InferContext,
                              inputDataFrameKnowledge: Knowledge[DataFrame],
                              transformerKnowledge: Knowledge[Transformer]
  ): (Knowledge[DataFrame], InferenceWarnings) = {
    val (transformedDataFrameKnowledge, transformedDataFrameWarnings) =
      transformerKnowledge.single.transform.infer(context)(())(inputDataFrameKnowledge)
    (transformedDataFrameKnowledge, transformedDataFrameWarnings)
  }

  private def inferTransformer(
                                context: InferContext,
                                estimatorKnowledge: Knowledge[Estimator[Transformer]],
                                inputDataFrameKnowledge: Knowledge[DataFrame]
  ): (Knowledge[Transformer], InferenceWarnings) = {
    throwIfToManyTypes(estimatorKnowledge)
    val estimator                                   = estimatorWithParams(estimatorKnowledge.single, context.graphReader)
    val (transformerKnowledge, transformerWarnings) =
      estimator.fit.infer(context)(())(inputDataFrameKnowledge)
    (transformerKnowledge, transformerWarnings)
  }

  private def throwIfToManyTypes(estimatorKnowledge: Knowledge[_]): Unit =
    if (estimatorKnowledge.size > 1)
      throw TooManyPossibleTypesException()

}
