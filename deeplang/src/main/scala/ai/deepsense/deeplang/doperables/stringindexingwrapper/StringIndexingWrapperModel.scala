package ai.deepsense.deeplang.doperables.stringindexingwrapper

import org.apache.spark.ml
import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import ai.deepsense.deeplang.doperables.report.Report
import ai.deepsense.deeplang.doperables.SparkModelWrapper
import ai.deepsense.deeplang.doperables.Transformer
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.params.Param
import ai.deepsense.deeplang.params.ParamMap

/** Model wrapper adding 'string indexing' behaviour.
  *
  * Concrete models (like GBTClassificationModel) must be concrete classes (leaves in hierarchy). That's why this class
  * must be abstract.
  */
abstract class StringIndexingWrapperModel[M <: ml.Model[M], E <: ml.Estimator[M]](
    private var wrappedModel: SparkModelWrapper[M, E]
) extends Transformer {

  private var pipelinedModel: PipelineModel = null

  private[stringindexingwrapper] def setPipelinedModel(pipelinedModel: PipelineModel): this.type = {
    this.pipelinedModel = pipelinedModel
    this
  }

  private[stringindexingwrapper] def setWrappedModel(wrappedModel: SparkModelWrapper[M, E]): this.type = {
    this.wrappedModel = wrappedModel
    this
  }

  final override def replicate(extra: ParamMap): this.type = {
    val newWrappedModel = wrappedModel.replicate(extra)
    // Assumption - spark objects underhood (and pipeline) remains the same
    super
      .replicate(extra)
      .setPipelinedModel(pipelinedModel)
      .setWrappedModel(newWrappedModel)
      .asInstanceOf[this.type]
  }

  override protected def applyTransform(ctx: ExecutionContext, df: DataFrame): DataFrame =
    DataFrame.fromSparkDataFrame(pipelinedModel.transform(df.sparkDataFrame))

  override protected def applyTransformSchema(schema: StructType, inferContext: InferContext): Option[StructType] =
    wrappedModel._transformSchema(schema, inferContext)

  override protected def applyTransformSchema(schema: StructType): Option[StructType] =
    wrappedModel._transformSchema(schema)

  override def report(extended: Boolean = true): Report = wrappedModel.report(extended)

  override def params: Array[Param[_]] = wrappedModel.params

  override protected def loadTransformer(ctx: ExecutionContext, path: String): this.type = {
    val pipelineModelPath   = Transformer.stringIndexerPipelineFilePath(path)
    val wrappedModelPath    = Transformer.stringIndexerWrappedModelFilePath(path)
    val loadedPipelineModel = PipelineModel.load(pipelineModelPath)
    val loadedWrappedModel  = Transformer.load(ctx, wrappedModelPath)
    this
      .setPipelinedModel(loadedPipelineModel)
      .setWrappedModel(loadedWrappedModel.asInstanceOf[SparkModelWrapper[M, E]])
      .setParamsFromJson(loadedWrappedModel.paramValuesToJson, ctx.inferContext.graphReader)
  }

  override protected def saveTransformer(ctx: ExecutionContext, path: String): Unit = {
    val pipelineModelPath = Transformer.stringIndexerPipelineFilePath(path)
    val wrappedModelPath  = Transformer.stringIndexerWrappedModelFilePath(path)
    pipelinedModel.save(pipelineModelPath)
    wrappedModel.save(ctx, wrappedModelPath)
  }

  override private[deeplang] def paramMap: ParamMap = wrappedModel.paramMap

  override private[deeplang] def defaultParamMap: ParamMap = wrappedModel.defaultParamMap

}
