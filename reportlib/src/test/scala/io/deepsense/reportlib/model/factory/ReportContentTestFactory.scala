package io.deepsense.reportlib.model.factory

import io.deepsense.reportlib.model.{ReportType, ReportContent}
import org.apache.spark.sql.types.{DoubleType, IntegerType, StructField, StructType}

trait ReportContentTestFactory {

  import ReportContentTestFactory._

  def testReport: ReportContent = ReportContent(
    reportName,
    reportType,
    Seq(TableTestFactory.testEmptyTable),
    Map(ReportContentTestFactory.categoricalDistName ->
      DistributionTestFactory.testCategoricalDistribution(
        ReportContentTestFactory.categoricalDistName),
      ReportContentTestFactory.continuousDistName ->
      DistributionTestFactory.testContinuousDistribution(
        ReportContentTestFactory.continuousDistName)
    )
  )

}

object ReportContentTestFactory extends ReportContentTestFactory {
  val continuousDistName = "continuousDistributionName"
  val categoricalDistName = "categoricalDistributionName"
  val reportName = "TestReportContentName"
  val reportType = ReportType.Empty

  val someReport: ReportContent = ReportContent("empty", ReportType.Empty)
}
