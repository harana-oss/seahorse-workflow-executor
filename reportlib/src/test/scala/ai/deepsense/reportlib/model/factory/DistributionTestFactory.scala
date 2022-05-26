package ai.deepsense.reportlib.model.factory

import ai.deepsense.reportlib.model.Distribution
import ai.deepsense.reportlib.model.DiscreteDistribution
import ai.deepsense.reportlib.model.ContinuousDistribution
import ai.deepsense.reportlib.model.Statistics

trait DistributionTestFactory {

  def testCategoricalDistribution: Distribution =
    testCategoricalDistribution(DistributionTestFactory.distributionName)

  def testCategoricalDistribution(name: String): Distribution =
    DiscreteDistribution(
      name,
      DistributionTestFactory.distributionDescription,
      DistributionTestFactory.distributionMissingValues,
      DistributionTestFactory.categoricalDistributionBuckets,
      DistributionTestFactory.distributionCounts
    )

  def testContinuousDistribution: Distribution =
    testContinuousDistribution(DistributionTestFactory.distributionName)

  def testContinuousDistribution(name: String): Distribution =
    ContinuousDistribution(
      name,
      DistributionTestFactory.distributionDescription,
      DistributionTestFactory.distributionMissingValues,
      DistributionTestFactory.continuousDistributionBuckets,
      DistributionTestFactory.distributionCounts,
      testStatistics
    )

  val testStatistics: Statistics = Statistics("43", "1.5", "12.1")

  val testStatisticsWithEmptyValues: Statistics = Statistics(None, Some("1.5"), None)

}

object DistributionTestFactory extends DistributionTestFactory {

  val distributionName: String = "Distribution Name"

  val distributionDescription: String = "Some distribution description"

  val distributionMissingValues: Long = 0L

  val categoricalDistributionBuckets: List[String] =
    List("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

  val continuousDistributionBuckets: List[String] = List("2", "2.5", "3", "3.5", "4", "4.5", "5", "5.5")

  val distributionCounts: List[Long] = List(0, 1000, 1, 17, 9786976976L, 0, 1)

}
