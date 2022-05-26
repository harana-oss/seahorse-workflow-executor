package io.deepsense.deeplang.doperables

import org.apache.spark.SparkException
import org.apache.spark.sql.types.StructType

import io.deepsense.commons.utils.Logging
import io.deepsense.deeplang._
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.doperables.serialization.Loadable
import io.deepsense.deeplang.doperables.serialization.ParamsSerialization
import io.deepsense.deeplang.doperables.serialization.PathsUtils
import io.deepsense.deeplang.doperations.exceptions.WriteFileException
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.inference.InferenceWarnings
import io.deepsense.deeplang.params.Params
import io.deepsense.reportlib.model.ReportType

/** Able to transform a DataFrame into another DataFrame. Can have mutable parameters. */
abstract class Transformer extends DOperable with Params with Logging with ParamsSerialization with Loadable {

  /** Creates a transformed DataFrame based on input DataFrame. */
  private[deeplang] def _transform(ctx: ExecutionContext, df: DataFrame): DataFrame

  /** Should be implemented in subclasses. For known schema of input DataFrame, infers schema of output DataFrame. If it
    * is not able to do it for some reasons, it returns None.
    */
  private[deeplang] def _transformSchema(schema: StructType): Option[StructType] = None

  /** Can be implemented in a subclass, if access to infer context is required. For known schema of input DataFrame,
    * infers schema of output DataFrame. If it is not able to do it for some reasons, it returns None.
    */
  private[deeplang] def _transformSchema(schema: StructType, inferContext: InferContext): Option[StructType] =
    _transformSchema(schema)

  def transform: DMethod1To1[Unit, DataFrame, DataFrame] =
    new DMethod1To1[Unit, DataFrame, DataFrame] {

      override def apply(ctx: ExecutionContext)(p: Unit)(df: DataFrame): DataFrame =
        _transform(ctx, df)

      override def infer(
          ctx: InferContext
      )(p: Unit)(k: DKnowledge[DataFrame]): (DKnowledge[DataFrame], InferenceWarnings) = {
        val df = DataFrame.forInference(k.single.schema.flatMap(s => _transformSchema(s, ctx)))
        (DKnowledge(df), InferenceWarnings.empty)
      }

    }

  override def report: Report =
    super.report
      .withReportName(s"$transformerName Report")
      .withReportType(ReportType.Model)
      .withAdditionalTable(CommonTablesGenerators.params(extractParamMap()))

  protected def transformerName: String = this.getClass.getSimpleName

  def save(ctx: ExecutionContext, path: String): Unit =
    try {
      saveObjectWithParams(ctx, path)
      saveTransformer(ctx, path)
    } catch {
      case e: SparkException =>
        logger.error(s"Saving Transformer error: Spark problem. Unable to write file to $path", e)
        throw WriteFileException(path, e)
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
