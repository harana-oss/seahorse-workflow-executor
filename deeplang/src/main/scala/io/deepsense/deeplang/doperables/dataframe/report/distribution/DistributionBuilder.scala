package io.deepsense.deeplang.doperables.dataframe.report.distribution

import org.apache.spark.sql.Row

import io.deepsense.deeplang.utils.aggregators.Aggregator
import io.deepsense.deeplang.utils.aggregators.AggregatorBatch.BatchedResult
import io.deepsense.reportlib.model.Distribution

trait DistributionBuilder {

  def allAggregators: Seq[Aggregator[_, Row]]

  def build(results: BatchedResult): Distribution

}
