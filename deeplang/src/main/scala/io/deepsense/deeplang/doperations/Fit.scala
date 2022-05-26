package io.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import spray.json.JsNull
import spray.json.JsValue

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.Estimator
import io.deepsense.deeplang.doperables.Transformer
import io.deepsense.deeplang.doperations.exceptions.TooManyPossibleTypesException
import io.deepsense.deeplang.doperations.layout.SmallBlockLayout2To1
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.inference.InferenceWarnings
import io.deepsense.deeplang.params.DynamicParam
import io.deepsense.deeplang.DKnowledge
import io.deepsense.deeplang.DOperation2To1
import io.deepsense.deeplang.ExecutionContext

case class Fit()
    extends DOperation2To1[Estimator[Transformer], DataFrame, Transformer]
    with SmallBlockLayout2To1
    with OperationDocumentation {

  override val id: Id = "0c2ff818-977b-11e5-8994-feff819cdc9f"

  override val name: String = "Fit"

  override val description: String =
    "Fits an Estimator on a DataFrame"

  override val since: Version = Version(1, 0, 0)

  val estimatorParams = new DynamicParam(
    name = "Parameters of input Estimator",
    description = Some("These parameters are rendered dynamically, depending on type of Estimator."),
    inputPort = 0
  )

  setDefault(estimatorParams -> JsNull)

  def setEstimatorParams(jsValue: JsValue): this.type = set(estimatorParams -> jsValue)

  override val params: Array[io.deepsense.deeplang.params.Param[_]] = Array(estimatorParams)

  override lazy val tTagTI_0: TypeTag[Estimator[Transformer]] = typeTag

  override lazy val tTagTI_1: TypeTag[DataFrame] = typeTag

  override lazy val tTagTO_0: TypeTag[Transformer] = typeTag

  override protected def execute(estimator: Estimator[Transformer], dataFrame: DataFrame)(
      ctx: ExecutionContext
  ): Transformer =
    estimatorWithParams(estimator).fit(ctx)(())(dataFrame)

  override protected def inferKnowledge(
      estimatorKnowledge: DKnowledge[Estimator[Transformer]],
      dataFrameKnowledge: DKnowledge[DataFrame]
  )(
      ctx: InferContext
  ): (DKnowledge[Transformer], InferenceWarnings) = {

    if (estimatorKnowledge.size > 1)
      throw TooManyPossibleTypesException()
    val estimator = estimatorKnowledge.single
    estimatorWithParams(estimator).fit.infer(ctx)(())(dataFrameKnowledge)
  }

  /** Note that DOperation should never mutate input DOperable. This method copies input estimator and sets parameters
    * in copy.
    */
  private def estimatorWithParams(estimator: Estimator[Transformer]): Estimator[Transformer] = {
    val estimatorWithParams = estimator
      .replicate()
      .setParamsFromJson($(estimatorParams), ignoreNulls = true)
    validateDynamicParams(estimatorWithParams)
    estimatorWithParams
  }

}
