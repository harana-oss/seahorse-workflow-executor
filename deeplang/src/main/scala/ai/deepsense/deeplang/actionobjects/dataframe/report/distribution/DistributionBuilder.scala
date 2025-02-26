package ai.deepsense.deeplang.actionobjects.dataframe.report.distribution

import org.apache.spark.sql.Row

import ai.deepsense.deeplang.utils.aggregators.Aggregator
import ai.deepsense.deeplang.utils.aggregators.AggregatorBatch.BatchedResult
import ai.deepsense.reportlib.model.Distribution

trait DistributionBuilder {

  def allAggregators: Seq[Aggregator[_, Row]]

  def build(results: BatchedResult): Distribution

}
