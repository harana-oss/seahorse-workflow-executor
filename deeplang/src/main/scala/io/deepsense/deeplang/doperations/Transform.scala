package io.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import spray.json.JsNull
import spray.json.JsValue

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.Transformer
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.layout.SmallBlockLayout2To1
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.inference.InferenceWarnings
import io.deepsense.deeplang.params.DynamicParam
import io.deepsense.deeplang.DKnowledge
import io.deepsense.deeplang.DOperation2To1
import io.deepsense.deeplang.ExecutionContext

case class Transform()
    extends DOperation2To1[Transformer, DataFrame, DataFrame]
    with SmallBlockLayout2To1
    with OperationDocumentation {

  override val id: Id = "643d8706-24db-4674-b5b4-10b5129251fc"

  override val name: String = "Transform"

  override val description: String =
    "Transforms a DataFrame using a Transformer"

  override val since: Version = Version(1, 0, 0)

  val transformerParams = new DynamicParam(
    name = "Parameters of input Transformer",
    description = Some("These parameters are rendered dynamically, depending on type of Transformer."),
    inputPort = 0
  )

  setDefault(transformerParams, JsNull)

  def getTransformerParams: JsValue = $(transformerParams)

  def setTransformerParams(jsValue: JsValue): this.type = set(transformerParams, jsValue)

  override val params: Array[io.deepsense.deeplang.params.Param[_]] = Array(transformerParams)

  override lazy val tTagTI_0: TypeTag[Transformer] = typeTag

  override lazy val tTagTI_1: TypeTag[DataFrame] = typeTag

  override lazy val tTagTO_0: TypeTag[DataFrame] = typeTag

  override protected def execute(transformer: Transformer, dataFrame: DataFrame)(context: ExecutionContext): DataFrame =
    transformerWithParams(transformer).transform(context)(())(dataFrame)

  override protected def inferKnowledge(
      transformerKnowledge: DKnowledge[Transformer],
      dataFrameKnowledge: DKnowledge[DataFrame]
  )(
      context: InferContext
  ): (DKnowledge[DataFrame], InferenceWarnings) =
    if (transformerKnowledge.size > 1)
      (DKnowledge(DataFrame.forInference()), InferenceWarnings.empty)
    else {
      val transformer = transformerKnowledge.single
      transformerWithParams(transformer).transform.infer(context)(())(dataFrameKnowledge)
    }

  private def transformerWithParams(transformer: Transformer): Transformer = {
    val transformerWithParams = transformer
      .replicate()
      .setParamsFromJson(getTransformerParams, ignoreNulls = true)
    validateDynamicParams(transformerWithParams)
    transformerWithParams
  }

}
