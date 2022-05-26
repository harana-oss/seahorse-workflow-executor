package io.deepsense.deeplang.doperables.report

import io.deepsense.deeplang.DOperable
import io.deepsense.reportlib.model.ReportType.ReportType
import io.deepsense.reportlib.model.ReportContent
import io.deepsense.reportlib.model.ReportType
import io.deepsense.reportlib.model.Table

case class Report(content: ReportContent = ReportContent("Empty Report", ReportType.Empty)) extends DOperable {

  def this() = this(ReportContent("Empty Report", ReportType.Empty))

  def withReportName(newName: String): Report =
    copy(content.copy(name = newName))

  def withReportType(newReportType: ReportType): Report =
    copy(content.copy(reportType = newReportType))

  def withAdditionalTable(table: Table, at: Int = 0): Report = {
    require(
      at <= content.tables.length && at >= 0,
      s"Table can be placed in possible position: [0; ${content.tables.length}]"
    )
    val (left, right) = content.tables.splitAt(at)
    val newTables     = left ++ Seq(table) ++ right
    copy(content.copy(tables = newTables))
  }

  override def report: Report = this

}
