package io.deepsense.deeplang.doperables.dataframe.report.distribution.continuous

import org.apache.spark.mllib.stat.MultivariateStatisticalSummary
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._

import io.deepsense.commons.datetime.DateTimeConverter
import io.deepsense.commons.utils.DoubleUtils
import io.deepsense.deeplang.doperables.dataframe.report.distribution.ColumnStats
import io.deepsense.deeplang.doperables.dataframe.report.distribution.DistributionBuilder
import io.deepsense.deeplang.utils.aggregators.Aggregator
import io.deepsense.deeplang.utils.aggregators.AggregatorBatch.BatchedResult
import io.deepsense.reportlib.model
import io.deepsense.reportlib.model.ContinuousDistribution
import io.deepsense.reportlib.model.Distribution

case class ContinuousDistributionBuilder(
    histogram: Aggregator[Array[Long], Row],
    missing: Aggregator[Long, Row],
    field: StructField,
    columnStats: ColumnStats
) extends DistributionBuilder {

  def allAggregators: Seq[Aggregator[_, Row]] = Seq(histogram, missing)

  override def build(results: BatchedResult): Distribution = {
    val buckets = BucketsCalculator.calculateBuckets(field.dataType, columnStats)

    val histogramCounts = results.forAggregator(histogram)
    val nullsCount      = results.forAggregator(missing)

    val labels = buckets2Labels(buckets, field)

    val stats = model.Statistics(
      double2Label(field)(columnStats.max),
      double2Label(field)(columnStats.min),
      mean2Label(field)(columnStats.mean)
    )

    ContinuousDistribution(
      field.name,
      s"Continuous distribution for ${field.name} column",
      nullsCount,
      labels,
      histogramCounts,
      stats
    )
  }

  private def buckets2Labels(buckets: Seq[Double], structField: StructField): Seq[String] =
    buckets.map(double2Label(structField))

  /** We want to present mean of integer-like values as a floating point number, however dates, timestamps and booleans
    * should be converted to their original type.
    */
  def mean2Label(structField: StructField)(d: Double): String = structField.dataType match {
    case ByteType | ShortType | IntegerType | LongType => DoubleUtils.double2String(d)
    case _                                             => double2Label(structField)(d)
  }

  def double2Label(structField: StructField)(d: Double): String =
    if (d.isNaN)
      "NaN"
    else
      structField.dataType match {
        case ByteType                                => d.toByte.toString
        case ShortType                               => d.toShort.toString
        case IntegerType                             => d.toInt.toString
        case LongType                                => d.toLong.toString
        case FloatType | DoubleType | _: DecimalType => DoubleUtils.double2String(d)
        case BooleanType                             => if (d == 0d) false.toString else true.toString
        case TimestampType | DateType =>
          DateTimeConverter.toString(DateTimeConverter.fromMillis(d.toLong))
      }

}
