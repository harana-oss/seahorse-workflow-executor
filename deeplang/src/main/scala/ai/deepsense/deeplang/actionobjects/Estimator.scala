package ai.deepsense.deeplang.actionobjects

import scala.reflect.runtime.universe.TypeTag

import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.DMethod1To1
import ai.deepsense.deeplang.ActionObject
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.reportlib.model.ReportType

/** Can create a Transformer of type T based on a DataFrame. */
abstract class Estimator[+T <: Transformer]()(implicit typeTag: TypeTag[T]) extends ActionObject with Params {

  def convertInputNumericToVector: Boolean = false

  def convertOutputVectorToDouble: Boolean = false

  /** Creates a Transformer based on a DataFrame. */
  private[deeplang] def _fit(ctx: ExecutionContext, df: DataFrame): T

  /** Creates an instance of Transformer for inference.
    *
    * @param schema
    *   the schema for inference, or None if it's unknown.
    */
  private[deeplang] def _fit_infer(schema: Option[StructType]): T

  def fit: DMethod1To1[Unit, DataFrame, T] = {
    new DMethod1To1[Unit, DataFrame, T] {
      override def apply(ctx: ExecutionContext)(p: Unit)(df: DataFrame): T =
        _fit(ctx, df)

      override def infer(ctx: InferContext)(p: Unit)(k: Knowledge[DataFrame]): (Knowledge[T], InferenceWarnings) = {
        val transformer = _fit_infer(k.single.schema)
        (Knowledge(transformer), InferenceWarnings.empty)
      }
    }
  }

  override def report(extended: Boolean = true): Report =
    super
      .report(extended)
      .withReportName(s"$estimatorName Report")
      .withReportType(ReportType.Estimator)
      .withAdditionalTable(CommonTablesGenerators.params(extractParamMap()))

  protected def estimatorName: String = this.getClass.getSimpleName

}
