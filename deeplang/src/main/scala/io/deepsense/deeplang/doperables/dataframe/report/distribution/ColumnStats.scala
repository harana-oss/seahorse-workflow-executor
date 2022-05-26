package io.deepsense.deeplang.doperables.dataframe.report.distribution

import org.apache.spark.mllib.stat.MultivariateStatisticalSummary

case class ColumnStats(min: Double, max: Double, mean: Double)

object ColumnStats {

  def fromMultiVarStats(multiVarStats: MultivariateStatisticalSummary, column: Int): ColumnStats =
    ColumnStats(multiVarStats.min(column), multiVarStats.max(column), multiVarStats.mean(column))

}
