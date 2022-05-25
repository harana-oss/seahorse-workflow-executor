package io.deepsense.deeplang.doperables

import org.apache.spark.sql.types.StructType
import spray.json.JsObject

import io.deepsense.deeplang._
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.serialization.{JsonObjectPersistence, PathsUtils}
import io.deepsense.deeplang.doperations.exceptions.CustomOperationExecutionException
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.params.custom.{InnerWorkflow, PublicParam}
import io.deepsense.deeplang.params.{Param, ParamMap, ParamPair}
import io.deepsense.deeplang.utils.CustomTransformerFactory
import io.deepsense.graph._

case class CustomTransformer(
    innerWorkflow: InnerWorkflow,
    publicParamsWithValues: Seq[ParamWithValues[_]])
  extends Transformer {

  def this() = this(InnerWorkflow.empty, Seq.empty)

  override val params: Array[Param[_]] = publicParamsWithValues.map(_.param).toArray

  publicParamsWithValues.foreach {
    case ParamWithValues(param, defaultValue, setValue) =>
      val paramAny = param.asInstanceOf[Param[Any]]
      defaultValue.foreach(defaultValue => defaultParamMap.put(ParamPair(paramAny, defaultValue)))
      setValue.foreach(setValue => paramMap.put(ParamPair(paramAny, setValue)))
  }

  override private[deeplang] def _transform(ctx: ExecutionContext, df: DataFrame): DataFrame = {
    ctx.innerWorkflowExecutor.execute(CommonExecutionContext(ctx), workflowWithParams(), df)
  }

  override private[deeplang] def _transformSchema(
      schema: StructType, inferCtx: InferContext): Option[StructType] = {
    val workflow = workflowWithParams()
    val initialKnowledge = GraphKnowledge(Map(
      workflow.source.id -> NodeInferenceResult(
        Vector(DKnowledge(DataFrame.forInference(schema))))
    ))

    val graphKnowledge = workflow.graph.inferKnowledge(inferCtx, initialKnowledge)

    if (graphKnowledge.errors.nonEmpty) {
      throw CustomOperationExecutionException(
        "Inner workflow contains errors:\n" +
          graphKnowledge.errors.values.flatten.map(_.toString).mkString("\n"))
    }

    graphKnowledge
      .getKnowledge(workflow.sink.id)(0).asInstanceOf[DKnowledge[DataFrame]].single.schema
  }

  override def replicate(extra: ParamMap = ParamMap.empty): this.type = {
    val that = new CustomTransformer(innerWorkflow, publicParamsWithValues).asInstanceOf[this.type]
    copyValues(that, extra)
  }

  private def workflowWithParams(): InnerWorkflow = {
    innerWorkflow.publicParams.foreach {
      case PublicParam(nodeId, paramName, publicName) =>
        val node = innerWorkflow.graph.node(nodeId)
        val operation = node.value
        val innerParam = getParam(operation.params, paramName).asInstanceOf[Param[Any]]
        operation.set(innerParam -> $(getParam(params, publicName)))
    }
    innerWorkflow
  }

  private def getParam(params: Array[Param[_]], name: String): Param[_] = {
    params.find(_.name == name).get
  }

  override protected def saveTransformer(ctx: ExecutionContext, path: String): Unit = {
    val innerWorkflowPath: String = CustomTransformer.innerWorkflowPath(path)
    val innerWorkflowJson: JsObject = ctx.innerWorkflowExecutor.toJson(innerWorkflow)
    JsonObjectPersistence.saveJsonToFile(ctx, innerWorkflowPath, innerWorkflowJson)
  }

  override protected def loadTransformer(
      ctx: ExecutionContext,
      path: String): CustomTransformer.this.type = {
    val innerWorkflowPath: String = CustomTransformer.innerWorkflowPath(path)
    val innerWorkflowJson = JsonObjectPersistence.loadJsonFromFile(ctx, innerWorkflowPath)
    CustomTransformerFactory.createCustomTransformer(
      ctx.innerWorkflowExecutor,
      innerWorkflowJson.asJsObject).asInstanceOf[this.type]
  }
}

object CustomTransformer {

  private val innerWorkflow = "innerWorkflow"

  def innerWorkflowPath(path: String): String = {
    PathsUtils.combinePaths(path, innerWorkflow)
  }
}

case class ParamWithValues[T](
  param: Param[_],
  defaultValue: Option[T] = None,
  setValue: Option[T] = None)
