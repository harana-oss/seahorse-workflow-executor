package io.deepsense.deeplang.doperables.dataframe.report.distribution.continuous

import org.apache.spark.mllib.stat.MultivariateStatisticalSummary
import org.apache.spark.sql.types.StructField

import io.deepsense.deeplang.doperables.dataframe.report.distribution._
import io.deepsense.deeplang.utils.SparkTypeConverter._
import io.deepsense.deeplang.utils.aggregators._

object ContinuousDistributionBuilderFactory {

  def prepareBuilder(
      columnIndex: Int,
      field: StructField,
      multivarStats: MultivariateStatisticalSummary): DistributionBuilder = {
    val columnStats = ColumnStats.fromMultiVarStats(multivarStats, columnIndex)
    // MultivarStats inits min with Double.MaxValue and max with MinValue.
    // If there is at least one not (null or NaN) its guaranteed to change min/max values.
    // TODO Its a bit hacky. Find more elegant solution. Example approaches:
    // - Filter out nulls? Problematic because we operate on vectors for performance.
    // - Remade spark aggregators to return options?
    val hasOnlyNulls =
      columnStats.min == Double.MaxValue &&
        columnStats.max == Double.MinValue

    if (!hasOnlyNulls) {
      val histogram = {
        val buckets = BucketsCalculator.calculateBuckets(field.dataType,
          columnStats)
        HistogramAggregator(buckets, true).mapInput(getColumnAsDouble(columnIndex))
      }
      val missing = CountOccurenceAggregator[Option[Any]](None).mapInput(getOption(columnIndex))
      val colStats = columnStats
      ContinuousDistributionBuilder(histogram, missing, field, colStats)
    } else {
      NoDistributionBuilder(field.name, NoDistributionReasons.OnlyNulls)
    }
  }
}
