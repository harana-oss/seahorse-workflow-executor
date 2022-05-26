package io.deepsense.deeplang.doperables

import scala.reflect.runtime.universe.TypeTag

import org.apache.spark.sql.types.StructType

import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.inference.InferenceWarnings
import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.DKnowledge
import io.deepsense.deeplang.DMethod1To1
import io.deepsense.deeplang.DOperable
import io.deepsense.deeplang.ExecutionContext
import io.deepsense.reportlib.model.ReportType

/** Can create a Transformer of type T based on a DataFrame. */
abstract class Estimator[+T <: Transformer]()(implicit typeTag: TypeTag[T]) extends DOperable with Params {

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

  def fit: DMethod1To1[Unit, DataFrame, T] =
    new DMethod1To1[Unit, DataFrame, T] {

      override def apply(ctx: ExecutionContext)(p: Unit)(df: DataFrame): T =
        _fit(ctx, df)

      override def infer(ctx: InferContext)(p: Unit)(k: DKnowledge[DataFrame]): (DKnowledge[T], InferenceWarnings) = {
        val transformer = _fit_infer(k.single.schema)
        (DKnowledge(transformer), InferenceWarnings.empty)
      }

    }

  override def report: Report =
    super.report
      .withReportName(s"$estimatorName Report")
      .withReportType(ReportType.Estimator)
      .withAdditionalTable(CommonTablesGenerators.params(extractParamMap()))

  protected def estimatorName: String = this.getClass.getSimpleName

}
