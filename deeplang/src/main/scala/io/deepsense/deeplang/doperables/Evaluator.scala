package io.deepsense.deeplang.doperables

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

/** Evaluates a DataFrame. */
abstract class Evaluator extends DOperable with Params {

  /** Evaluates a DataFrame. */
  private[deeplang] def _evaluate(context: ExecutionContext, dataFrame: DataFrame): MetricValue

  private[deeplang] def _infer(k: DKnowledge[DataFrame]): MetricValue

  def evaluate: DMethod1To1[Unit, DataFrame, MetricValue] =
    new DMethod1To1[Unit, DataFrame, MetricValue] {

      override def apply(ctx: ExecutionContext)(p: Unit)(dataFrame: DataFrame): MetricValue =
        _evaluate(ctx, dataFrame)

      override def infer(ctx: InferContext)(p: Unit)(
          k: DKnowledge[DataFrame]
      ): (DKnowledge[MetricValue], InferenceWarnings) =
        (DKnowledge(_infer(k)), InferenceWarnings.empty)

    }

  override def report: Report =
    super.report
      .withReportName(s"${this.getClass.getSimpleName} Report")
      .withReportType(ReportType.Evaluator)
      .withAdditionalTable(CommonTablesGenerators.params(extractParamMap()))

  def isLargerBetter: Boolean

}
