package ai.deepsense.deeplang.actionobjects

import org.apache.spark.SparkException
import org.apache.spark.sql.types.StructType

import ai.deepsense.commons.utils.Logging
import ai.deepsense.deeplang._
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actionobjects.serialization.Loadable
import ai.deepsense.deeplang.actionobjects.serialization.ParamsSerialization
import ai.deepsense.deeplang.actionobjects.serialization.PathsUtils
import ai.deepsense.deeplang.actions.exceptions.WriteFileException
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.reportlib.model.ReportType

/** Able to transform a DataFrame into another DataFrame. Can have mutable parameters. */
abstract class Transformer extends ActionObject with Params with Logging with ParamsSerialization with Loadable {

  /** Creates a transformed DataFrame based on input DataFrame. */
  protected def applyTransform(ctx: ExecutionContext, df: DataFrame): DataFrame

  /** Library internal direct access to `applyTransform`. */
  final private[deeplang] def _transform(ctx: ExecutionContext, df: DataFrame): DataFrame = applyTransform(ctx, df)

  /** Should be implemented in subclasses. For known schema of input DataFrame, infers schema of output DataFrame. If it
    * is not able to do it for some reasons, it returns None.
    */
  protected def applyTransformSchema(schema: StructType): Option[StructType] = None

  /** Library internal direct access to `applyTransformSchema`. */
  final private[deeplang] def _transformSchema(schema: StructType): Option[StructType] = applyTransformSchema(schema)

  /** Can be implemented in a subclass, if access to infer context is required. For known schema of input DataFrame,
    * infers schema of output DataFrame. If it is not able to do it for some reasons, it returns None.
    */
  protected def applyTransformSchema(schema: StructType, inferContext: InferContext): Option[StructType] =
    applyTransformSchema(schema)

  /** Library internal direct access to `applyTransformSchema`. */
  final private[deeplang] def _transformSchema(schema: StructType, inferContext: InferContext): Option[StructType] =
    applyTransformSchema(schema, inferContext)

  def transform: DMethod1To1[Unit, DataFrame, DataFrame] = {
    new DMethod1To1[Unit, DataFrame, DataFrame] {
      override def apply(ctx: ExecutionContext)(p: Unit)(df: DataFrame): DataFrame =
        applyTransform(ctx, df)

      override def infer(
          ctx: InferContext
      )(p: Unit)(k: Knowledge[DataFrame]): (Knowledge[DataFrame], InferenceWarnings) = {
        val df = DataFrame.forInference(k.single.schema.flatMap(s => applyTransformSchema(s, ctx)))
        (Knowledge(df), InferenceWarnings.empty)
      }
    }
  }

  override def report(extended: Boolean = true): Report =
    super
      .report(extended)
      .withReportName(s"$transformerName Report")
      .withReportType(ReportType.Model)
      .withAdditionalTable(CommonTablesGenerators.params(extractParamMap()))

  protected def transformerName: String = this.getClass.getSimpleName

  def save(ctx: ExecutionContext, path: String): Unit = {
    try {
      saveObjectWithParams(ctx, path)
      saveTransformer(ctx, path)
    } catch {
      case e: SparkException =>
        logger.error(s"Saving Transformer error: Spark problem. Unable to write file to $path", e)
        throw WriteFileException(path, e)
    }
  }

  override def load(ctx: ExecutionContext, path: String): this.type =
    loadTransformer(ctx, path).loadAndSetParams(ctx, path)

  protected def saveTransformer(ctx: ExecutionContext, path: String): Unit = {}

  protected def loadTransformer(ctx: ExecutionContext, path: String): this.type =
    this

}

object Transformer extends Logging {

  private val modelFilePath = "deepsenseModel"

  private val transformerFilePath = "deepsenseTransformer"

  private val parentEstimatorFilePath = "deepsenseParentEstimator"

  private val pipelineFilePath = "deepsensePipeline"

  private val wrappedModelFilePath = "deepsenseWrappedModel"

  def load(ctx: ExecutionContext, path: String): Transformer = {
    logger.debug("Loading transformer from: {}", path)
    ParamsSerialization.load(ctx, path).asInstanceOf[Transformer]
  }

  def modelFilePath(path: String): String =
    PathsUtils.combinePaths(path, modelFilePath)

  def parentEstimatorFilePath(path: String): String =
    PathsUtils.combinePaths(path, parentEstimatorFilePath)

  def stringIndexerPipelineFilePath(path: String): String =
    PathsUtils.combinePaths(modelFilePath(path), pipelineFilePath)

  def stringIndexerWrappedModelFilePath(path: String): String =
    PathsUtils.combinePaths(modelFilePath(path), wrappedModelFilePath)

  def transformerSparkTransformerFilePath(path: String): String =
    PathsUtils.combinePaths(path, transformerFilePath)

}
