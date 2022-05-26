package io.deepsense.deeplang.doperables.dataframe.report.distribution

import org.apache.spark.sql.Row

import io.deepsense.deeplang.utils.aggregators.Aggregator
import io.deepsense.deeplang.utils.aggregators.AggregatorBatch.BatchedResult
import io.deepsense.reportlib.model.Distribution
import io.deepsense.reportlib.model.NoDistribution

case class NoDistributionBuilder(name: String, description: String) extends DistributionBuilder {

  override def allAggregators: Seq[Aggregator[_, Row]] = Nil

  override def build(results: BatchedResult): Distribution =
    NoDistribution(name, description)

}
