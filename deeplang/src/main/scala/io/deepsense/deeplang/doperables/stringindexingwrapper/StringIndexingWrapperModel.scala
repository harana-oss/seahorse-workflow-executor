package io.deepsense.deeplang.doperables.stringindexingwrapper

import org.apache.spark.ml
import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.types.StructType

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.doperables.{SparkModelWrapper, Transformer}
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.params.{Param, ParamMap}

/**
  * Model wrapper adding 'string indexing' behaviour.
  *
  * Concrete models (like GBTClassificationModel) must be concrete classes (leaves in hierarchy).
  * That's why this class must be abstract.
  */
abstract class StringIndexingWrapperModel[M <: ml.Model[M], E <: ml.Estimator[M]](
    private var wrappedModel: SparkModelWrapper[M, E]) extends Transformer {

  private var pipelinedModel: PipelineModel = null

  private[stringindexingwrapper] def setPipelinedModel(
      pipelinedModel: PipelineModel): this.type = {
    this.pipelinedModel = pipelinedModel
    this
  }

  private[stringindexingwrapper] def setWrappedModel(
      wrappedModel: SparkModelWrapper[M, E]): this.type = {
    this.wrappedModel = wrappedModel
    this
  }

  override final def replicate(extra: ParamMap): this.type = {
    val newWrappedModel = wrappedModel.replicate(extra)
    // Assumption - spark objects underhood (and pipeline) remains the same
    super.replicate(extra)
      .setPipelinedModel(pipelinedModel)
      .setWrappedModel(newWrappedModel)
      .asInstanceOf[this.type]
  }

  override private[deeplang] def _transform(ctx: ExecutionContext, df: DataFrame): DataFrame = {
    DataFrame.fromSparkDataFrame(pipelinedModel.transform(df.sparkDataFrame))
  }

  override private[deeplang] def _transformSchema(
      schema: StructType, inferContext: InferContext): Option[StructType] =
    wrappedModel._transformSchema(schema, inferContext)

  override private[deeplang] def _transformSchema(schema: StructType): Option[StructType] =
    wrappedModel._transformSchema(schema)

  override def report: Report = wrappedModel.report

  override def params: Array[Param[_]] = wrappedModel.params

  override protected def loadTransformer(ctx: ExecutionContext, path: String): this.type = {
    val pipelineModelPath = Transformer.stringIndexerPipelineFilePath(path)
    val wrappedModelPath = Transformer.stringIndexerWrappedModelFilePath(path)
    val loadedPipelineModel = PipelineModel.load(pipelineModelPath)
    val loadedWrappedModel = Transformer.load(ctx, wrappedModelPath)

    this
      .setPipelinedModel(loadedPipelineModel)
      .setWrappedModel(loadedWrappedModel.asInstanceOf[SparkModelWrapper[M, E]])
      .setParamsFromJson(loadedWrappedModel.paramValuesToJson)
  }

  override protected def saveTransformer(ctx: ExecutionContext, path: String): Unit = {
    val pipelineModelPath = Transformer.stringIndexerPipelineFilePath(path)
    val wrappedModelPath = Transformer.stringIndexerWrappedModelFilePath(path)
    pipelinedModel.save(pipelineModelPath)
    wrappedModel.save(ctx, wrappedModelPath)
  }

  private[deeplang] override def paramMap: ParamMap = wrappedModel.paramMap

  private[deeplang] override def defaultParamMap: ParamMap = wrappedModel.defaultParamMap

}
