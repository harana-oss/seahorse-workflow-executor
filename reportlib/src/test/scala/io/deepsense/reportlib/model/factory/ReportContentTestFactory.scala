package io.deepsense.reportlib.model.factory

import io.deepsense.reportlib.model.ReportType
import io.deepsense.reportlib.model.ReportContent
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

trait ReportContentTestFactory {

  import ReportContentTestFactory._

  def testReport: ReportContent = ReportContent(
    reportName,
    reportType,
    Seq(TableTestFactory.testEmptyTable),
    Map(
      ReportContentTestFactory.categoricalDistName ->
        DistributionTestFactory.testCategoricalDistribution(ReportContentTestFactory.categoricalDistName),
      ReportContentTestFactory.continuousDistName ->
        DistributionTestFactory.testContinuousDistribution(ReportContentTestFactory.continuousDistName)
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
