package io.deepsense.deeplang.doperables.dataframe.report.distribution.discrete

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{BooleanType, StringType, StructField}

import io.deepsense.deeplang.doperables.dataframe.report.DataFrameReportGenerator
import io.deepsense.deeplang.doperables.dataframe.report.distribution.{DistributionBuilder, NoDistributionReasons}
import io.deepsense.deeplang.doperables.report.ReportUtils
import io.deepsense.deeplang.utils.aggregators.Aggregator
import io.deepsense.deeplang.utils.aggregators.AggregatorBatch.BatchedResult
import io.deepsense.reportlib.model.{DiscreteDistribution, Distribution, NoDistribution}

case class DiscreteDistributionBuilder(
    categories: Aggregator[Option[scala.collection.mutable.Map[String, Long]], Row],
    missing: Aggregator[Long, Row],
    field: StructField)
  extends DistributionBuilder {

  def allAggregators: Seq[Aggregator[_, Row]] = Seq(categories, missing)

  override def build(results: BatchedResult): Distribution = {
    val categoriesMap = results.forAggregator(categories)
    val nullsCount = results.forAggregator(missing)

    categoriesMap match {
      case Some(occurrencesMap) => {
        val labels = field.dataType match {
          case StringType => occurrencesMap.keys.toSeq.sorted
          // We always want two labels, even when all elements are true or false
          case BooleanType => Seq(false.toString, true.toString)
        }
        val counts = labels.map(occurrencesMap.getOrElse(_, 0L))
        DiscreteDistribution(
          field.name,
          s"Discrete distribution for ${field.name} column",
          nullsCount,
          labels.map(ReportUtils.shortenLongStrings(_,
            DataFrameReportGenerator.StringPreviewMaxLength)),
          counts)
      }
      case None => NoDistribution(
        field.name,
        NoDistributionReasons.TooManyDistinctCategoricalValues
      )
    }
  }
}


