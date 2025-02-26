package ai.deepsense.deeplang.actionobjects.dataframe.report.distribution.discrete

import org.apache.spark.sql.types.StructField

import ai.deepsense.deeplang.utils.SparkTypeConverter._
import ai.deepsense.deeplang.utils.aggregators._

private[distribution] object DiscreteDistributionBuilderFactory {

  val MaxDistinctValuesToCalculateDistribution = 10

  def prepareBuilder(columnIndex: Int, field: StructField): DiscreteDistributionBuilder = {
    val missing = CountOccurenceAggregator[Option[Any]](None)
      .mapInput(getOption(columnIndex))

    val categories = CountOccurrencesWithKeyLimitAggregator(
      MaxDistinctValuesToCalculateDistribution
    ).mapInput(getColumnAsString(columnIndex))

    DiscreteDistributionBuilder(categories, missing, field)
  }

}
