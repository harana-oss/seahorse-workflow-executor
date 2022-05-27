package ai.deepsense.deeplang.actionobjects

import ai.deepsense.commons.types.ColumnType
import ai.deepsense.commons.utils.DoubleUtils
import ai.deepsense.deeplang.ActionObject
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.reportlib.model.ReportType
import ai.deepsense.reportlib.model.Table

/** Metric value.
  *
  * @param name
  *   name of the metric (e.g. RMSE).
  * @param value
  *   value.
  */
case class MetricValue(name: String, value: Double) extends ActionObject {

  def this() = this(null, Double.NaN)

  override def report(extended: Boolean = true): Report =
    super
      .report(extended)
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
