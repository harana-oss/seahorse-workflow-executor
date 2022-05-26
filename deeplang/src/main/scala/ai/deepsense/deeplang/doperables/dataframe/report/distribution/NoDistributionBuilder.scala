package ai.deepsense.deeplang.doperables.dataframe.report.distribution

import org.apache.spark.sql.Row

import ai.deepsense.deeplang.utils.aggregators.Aggregator
import ai.deepsense.deeplang.utils.aggregators.AggregatorBatch.BatchedResult
import ai.deepsense.reportlib.model.Distribution
import ai.deepsense.reportlib.model.NoDistribution

case class NoDistributionBuilder(name: String, description: String) extends DistributionBuilder {

  override def allAggregators: Seq[Aggregator[_, Row]] = Nil

  override def build(results: BatchedResult): Distribution =
    NoDistribution(name, description)

}
