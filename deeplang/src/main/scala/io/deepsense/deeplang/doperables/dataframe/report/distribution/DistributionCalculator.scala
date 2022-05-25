package io.deepsense.deeplang.doperables.dataframe.report.distribution

import org.apache.spark.mllib.stat.MultivariateStatisticalSummary
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types._

import io.deepsense.deeplang.doperables.dataframe.report.distribution.continuous.ContinuousDistributionBuilderFactory
import io.deepsense.deeplang.doperables.dataframe.report.distribution.discrete.DiscreteDistributionBuilderFactory
import io.deepsense.deeplang.utils.aggregators.AggregatorBatch
import io.deepsense.reportlib.model._

object DistributionCalculator {

  def distributionByColumn(
    sparkDataFrame: org.apache.spark.sql.DataFrame,
    multivarStats: MultivariateStatisticalSummary): Map[String, Distribution] = {
    val dataFrameEmpty = multivarStats.count == 0

    if (dataFrameEmpty) {
      noDistributionBecauseOfNoData(sparkDataFrame.schema)
    } else {
      distributionForNonEmptyDataFrame(sparkDataFrame, multivarStats)
    }
  }

  private def noDistributionBecauseOfNoData(schema: StructType): Map[String, Distribution] = {
    for (columnName <- schema.fieldNames) yield {
      columnName -> NoDistribution(
        columnName,
        NoDistributionReasons.NoData
      )
    }
  }.toMap

  /**
   * Some calculations needed to obtain distributions can be performed together which
   * would result in only one pass over data.
   * <p>
   * To achieve that 'Aggregator' abstraction was introduced.
   * It contains all methods needed for rdd::aggregate method.
   * By abstracting it it's possible to batch together aggregators in a generic way.
   * <p>
   * Factory classes returns BUILDERS that have all data needed for manufacturing Distributions
   * except for data needed to be calculated on clusters. Builders expose their
   * internal aggregators for those instead.
   * <p>
   * All aggregators from builders are collected here, batched together and calculated in one pass.
   * Then batched result is passed to DistributionBuilders and final Distributions objects
   * are made.
   */
  private def distributionForNonEmptyDataFrame(
    sparkDataFrame: DataFrame,
    multivarStats: MultivariateStatisticalSummary): Map[String, Distribution] = {
    val schema = sparkDataFrame.schema

    val distributionBuilders = for {
      (structField, columnIndex) <- sparkDataFrame.schema.zipWithIndex
    } yield {
      DistributionType.forStructField(structField) match {
        case DistributionType.Discrete =>
          DiscreteDistributionBuilderFactory.prepareBuilder(columnIndex, structField)
        case DistributionType.Continuous =>
          ContinuousDistributionBuilderFactory.prepareBuilder(
            columnIndex, structField, multivarStats)
        case DistributionType.NotApplicable => NoDistributionBuilder(
          structField.name,
          NoDistributionReasons.NotApplicableForType(structField.dataType))
      }
    }
    val results = {
      val aggregators = distributionBuilders.flatMap(_.allAggregators)
      AggregatorBatch.executeInBatch(sparkDataFrame.rdd, aggregators)
    }
    val distributions = distributionBuilders.map(_.build(results))
    distributions.map(d => d.name -> d).toMap
  }
}
