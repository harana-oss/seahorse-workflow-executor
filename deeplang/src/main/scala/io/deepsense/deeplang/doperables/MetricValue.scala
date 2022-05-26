package io.deepsense.deeplang.doperables

import io.deepsense.commons.types.ColumnType
import io.deepsense.commons.utils.DoubleUtils
import io.deepsense.deeplang.DOperable
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.reportlib.model.ReportType
import io.deepsense.reportlib.model.Table

/** Metric value.
  *
  * @param name
  *   name of the metric (e.g. RMSE).
  * @param value
  *   value.
  */
case class MetricValue(name: String, value: Double) extends DOperable {

  def this() = this(null, Double.NaN)

  override def report: Report =
    super.report
      .withReportName("Report for Metric Value")
      .withReportType(ReportType.MetricValue)
      .withAdditionalTable(
        Table(
          name = "Metric Value",
          description = "",
          columnNames = Some(List(name)),
          columnTypes = List(ColumnType.string),
          rowNames = None,
          values = List(List(Some(DoubleUtils.double2String(value))))
        )
      )

}

object MetricValue {

  def forInference(name: String): MetricValue = MetricValue(name, Double.NaN)

}
