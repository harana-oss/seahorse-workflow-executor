package io.deepsense.deeplang.doperations

import scala.reflect.runtime.universe._

import spray.json.JsNull
import spray.json.JsValue

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.Estimator
import io.deepsense.deeplang.doperables.Transformer
import io.deepsense.deeplang.doperations.exceptions.TooManyPossibleTypesException
import io.deepsense.deeplang.doperations.layout.SmallBlockLayout2To2
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.inference.InferenceWarnings
import io.deepsense.deeplang.params.DynamicParam
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.DKnowledge
import io.deepsense.deeplang.DOperation2To2
import io.deepsense.deeplang.ExecutionContext

class FitPlusTransform
    extends DOperation2To2[Estimator[Transformer], DataFrame, DataFrame, Transformer]
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

  val estimatorParams = new DynamicParam(
    name = "Parameters of input Estimator",
    description = Some("These parameters are rendered dynamically, depending on type of Estimator."),
    inputPort = 0
  )

  setDefault(estimatorParams -> JsNull)

  def setEstimatorParams(jsValue: JsValue): this.type = set(estimatorParams -> jsValue)

  override val params: Array[Param[_]] = Array(estimatorParams)

  override protected def execute(estimator: Estimator[Transformer], dataFrame: DataFrame)(
      context: ExecutionContext
  ): (DataFrame, Transformer) = {
    val estimatorToRun           = estimatorWithParams(estimator)
    val transformer: Transformer = estimatorToRun.fit(context)(())(dataFrame)
    val transformed: DataFrame   = transformer.transform(context)(())(dataFrame)
    (transformed, transformer)
  }

  override protected def inferKnowledge(
      estimatorKnowledge: DKnowledge[Estimator[Transformer]],
      inputDataFrameKnowledge: DKnowledge[DataFrame]
  )(
      context: InferContext
  ): ((DKnowledge[DataFrame], DKnowledge[Transformer]), InferenceWarnings) = {

    val (transformerKnowledge, transformerWarnings) =
      inferTransformer(context, estimatorKnowledge, inputDataFrameKnowledge)

    val (transformedDataFrameKnowledge, transformedDataFrameWarnings) =
      inferDataFrame(context, inputDataFrameKnowledge, transformerKnowledge)

    val warningsSum: InferenceWarnings = transformerWarnings ++ transformedDataFrameWarnings
    ((transformedDataFrameKnowledge, transformerKnowledge), warningsSum)
  }

  private def estimatorWithParams(estimator: Estimator[Transformer]): Estimator[Transformer] = {
    val estimatorWithParams = estimator
      .replicate()
      .setParamsFromJson($(estimatorParams), ignoreNulls = true)
    validateDynamicParams(estimatorWithParams)
    estimatorWithParams
  }

  private def inferDataFrame(
      context: InferContext,
      inputDataFrameKnowledge: DKnowledge[DataFrame],
      transformerKnowledge: DKnowledge[Transformer]
  ): (DKnowledge[DataFrame], InferenceWarnings) = {
    val (transformedDataFrameKnowledge, transformedDataFrameWarnings) =
      transformerKnowledge.single.transform.infer(context)(())(inputDataFrameKnowledge)
    (transformedDataFrameKnowledge, transformedDataFrameWarnings)
  }

  private def inferTransformer(
      context: InferContext,
      estimatorKnowledge: DKnowledge[Estimator[Transformer]],
      inputDataFrameKnowledge: DKnowledge[DataFrame]
  ): (DKnowledge[Transformer], InferenceWarnings) = {
    throwIfToManyTypes(estimatorKnowledge)
    val estimator = estimatorWithParams(estimatorKnowledge.single)
    val (transformerKnowledge, transformerWarnings) =
      estimator.fit.infer(context)(())(inputDataFrameKnowledge)
    (transformerKnowledge, transformerWarnings)
  }

  private def throwIfToManyTypes(estimatorKnowledge: DKnowledge[_]): Unit =
    if (estimatorKnowledge.size > 1)
      throw TooManyPossibleTypesException()

}
