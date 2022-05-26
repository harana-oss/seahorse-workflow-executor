package ai.deepsense.reportlib.model

import ai.deepsense.reportlib.model.ReportType.ReportType

case class ReportContent(
    name: String,
    reportType: ReportType,
    tables: Seq[Table] = Seq.empty,
    distributions: Map[String, Distribution] = Map()
) {

  def tableByName(name: String): Option[Table] = tables.find(_.name == name)

}
