package ai.deepsense.deeplang.doperables

import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators
import ai.deepsense.deeplang.doperables.report.Report
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.params.Params
import ai.deepsense.deeplang.DKnowledge
import ai.deepsense.deeplang.DMethod1To1
import ai.deepsense.deeplang.DOperable
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.reportlib.model.ReportType

/** Evaluates a DataFrame. */
abstract class Evaluator extends DOperable with Params {

  /** Evaluates a DataFrame. */
  private[deeplang] def _evaluate(context: ExecutionContext, dataFrame: DataFrame): MetricValue

  private[deeplang] def _infer(k: DKnowledge[DataFrame]): MetricValue

  def evaluate: DMethod1To1[Unit, DataFrame, MetricValue] = {
    new DMethod1To1[Unit, DataFrame, MetricValue] {
      override def apply(ctx: ExecutionContext)(p: Unit)(dataFrame: DataFrame): MetricValue =
        _evaluate(ctx, dataFrame)

      override def infer(ctx: InferContext)(p: Unit)(
          k: DKnowledge[DataFrame]
      ): (DKnowledge[MetricValue], InferenceWarnings) =
        (DKnowledge(_infer(k)), InferenceWarnings.empty)
    }
  }

  override def report(extended: Boolean = true): Report =
    super
      .report(extended)
      .withReportName(s"${this.getClass.getSimpleName} Report")
      .withReportType(ReportType.Evaluator)
      .withAdditionalTable(CommonTablesGenerators.params(extractParamMap()))

  def isLargerBetter: Boolean

}
