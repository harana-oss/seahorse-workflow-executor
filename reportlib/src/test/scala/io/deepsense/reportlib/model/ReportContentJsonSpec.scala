package io.deepsense.reportlib.model

import io.deepsense.reportlib.model.factory.ReportContentTestFactory
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import spray.json._

class ReportContentJsonSpec extends AnyWordSpec with Matchers with ReportContentTestFactory with ReportJsonProtocol {

  import ReportContentTestFactory._

  "ReportContent" should {

    val emptyReportJson: JsObject = JsObject(
      "name"          -> JsString(reportName),
      "reportType"    -> JsString(reportType.toString),
      "tables"        -> JsArray(),
      "distributions" -> JsObject()
    )
    val report = testReport
    val reportJson: JsObject = JsObject(
      "name"          -> JsString(reportName),
      "reportType"    -> JsString(reportType.toString),
      "tables"        -> JsArray(report.tables.map(_.toJson): _*),
      "distributions" -> JsObject(report.distributions.mapValues(_.toJson))
    )

    "serialize" when {
      "empty" in {
        val report = ReportContent(reportName, reportType)
        report.toJson shouldBe emptyReportJson
      }
      "filled report" in {
        val json = report.toJson
        json shouldBe reportJson
      }
    }
    "deserialize" when {
      "empty report" in {
        emptyReportJson.convertTo[ReportContent] shouldBe ReportContent(reportName, reportType)
      }
      "full report" in {
        reportJson.convertTo[ReportContent] shouldBe report
      }
    }
  }

}
