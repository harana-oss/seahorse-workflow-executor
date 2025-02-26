package ai.deepsense.reportlib.model

import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import spray.json._

import ai.deepsense.reportlib.model.factory.DistributionTestFactory

class DistributionJsonSpec
    extends AnyWordSpec
    with MockitoSugar
    with DistributionTestFactory
    with Matchers
    with ReportJsonProtocol {

  "NoDistribution" should {
    val noDistribution: Distribution = NoDistribution(
      DistributionTestFactory.distributionName,
      DistributionTestFactory.distributionDescription
    )
    val jsonNoDistribution: JsObject = JsObject(
      "name"          -> JsString(DistributionTestFactory.distributionName),
      "subtype"       -> JsString("no_distribution"),
      "description"   -> JsString(DistributionTestFactory.distributionDescription),
      "missingValues" -> JsNumber(0)
    )
    "serialize to Json" in {
      val json = noDistribution.toJson
      json shouldBe jsonNoDistribution
    }
    "deserialize from Json" in {
      val distributionObject = jsonNoDistribution.convertTo[Distribution]
      distributionObject shouldBe noDistribution
    }
  }

  "DiscreteDistribution" should {
    val jsonCategoricalDistribution: JsObject = JsObject(
      "name"          -> JsString(DistributionTestFactory.distributionName),
      "subtype"       -> JsString("discrete"),
      "description"   -> JsString(DistributionTestFactory.distributionDescription),
      "missingValues" -> JsNumber(0),
      "buckets"       ->
        JsArray(DistributionTestFactory.categoricalDistributionBuckets.map(JsString(_)).toVector),
      "counts"        -> JsArray(DistributionTestFactory.distributionCounts.map(JsNumber(_)).toVector)
    )
    "serialize to Json" in {
      val json = testCategoricalDistribution.toJson
      json shouldBe jsonCategoricalDistribution
    }
    "deserialize from Json" in {
      jsonCategoricalDistribution.convertTo[Distribution] shouldBe testCategoricalDistribution
    }
  }

  "ContinuousDistribution" should {
    val statistics                           = testStatistics
    val jsonContinuousDistribution: JsObject = JsObject(
      "name"          -> JsString(DistributionTestFactory.distributionName),
      "subtype"       -> JsString("continuous"),
      "description"   -> JsString(DistributionTestFactory.distributionDescription),
      "missingValues" -> JsNumber(0),
      "buckets"       ->
        JsArray(DistributionTestFactory.continuousDistributionBuckets.map(JsString(_)).toVector),
      "counts"        -> JsArray(DistributionTestFactory.distributionCounts.map(JsNumber(_)).toVector),
      "statistics"    -> expectedStatisticsJson(statistics)
    )
    "serialize to Json" in {
      val json = testContinuousDistribution.toJson
      json shouldBe jsonContinuousDistribution
    }
    "deserialize from Json" in {
      jsonContinuousDistribution.convertTo[Distribution] shouldBe testContinuousDistribution
    }
    "throw IllegalArgumentException" when {
      def createContinousDistributionWith(buckets: Seq[String], counts: Seq[Long]): ContinuousDistribution =
        ContinuousDistribution("", "", 1, buckets, counts, testStatistics)
      "created with empty buckets and single count" in {
        an[IllegalArgumentException] shouldBe thrownBy(createContinousDistributionWith(Seq(), Seq(1)))
      }
      "created with buckets of size one" in {
        an[IllegalArgumentException] shouldBe thrownBy(createContinousDistributionWith(Seq("1"), Seq()))
      }
      "created with non empty buckets and counts of size != (buckets' size -1)" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          createContinousDistributionWith(Seq("0.1", "0.2", "0.3"), Seq(1))
        )
      }
    }
  }

  "Statistics" should {
    val statisticsWithEmptyValues = testStatisticsWithEmptyValues
    "serialize to Json" in {
      val json = statisticsWithEmptyValues.toJson
      json shouldBe expectedStatisticsJson(statisticsWithEmptyValues)
    }
    "deserialize from Json" in {
      expectedStatisticsJson(statisticsWithEmptyValues).convertTo[Statistics] shouldBe
        statisticsWithEmptyValues
    }
  }

  private def expectedStatisticsJson(statistics: Statistics): JsObject =
    JsObject(
      "max"  -> jsStringOrNull(statistics.max),
      "min"  -> jsStringOrNull(statistics.min),
      "mean" -> jsStringOrNull(statistics.mean)
    )

  private def jsStringOrNull(s: Option[String]): JsValue = s.map(JsString(_)).getOrElse(JsNull)

}
