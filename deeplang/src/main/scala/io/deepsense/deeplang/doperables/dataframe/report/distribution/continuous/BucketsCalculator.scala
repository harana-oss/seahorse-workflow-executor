package io.deepsense.deeplang.doperables.dataframe.report.distribution.continuous

import org.apache.spark.sql.types._

import io.deepsense.deeplang.doperables.dataframe.report.distribution.ColumnStats

object BucketsCalculator {

  val DefaultBucketsNumber = 20
  val DoubleTolerance = 0.000001

  def calculateBuckets(dataType: DataType, columnStats: ColumnStats): Array[Double] = {
    val steps = numberOfSteps(columnStats, dataType)
    customRange(columnStats.min, columnStats.max, steps)
  }

  private def numberOfSteps(columnStats: ColumnStats, dataType: DataType): Int =
    if (columnStats.max - columnStats.min < DoubleTolerance) {
      1
    } else if (isIntegerLike(dataType)) {
      Math.min(
        columnStats.max.toLong - columnStats.min.toLong + 1,
        DefaultBucketsNumber).toInt
    } else {
      DefaultBucketsNumber
    }

  private def customRange(min: Double, max: Double, steps: Int): Array[Double] = {
    val span = max - min
    (Range.Int(0, steps, 1).map(s => min + (s * span) / steps) :+ max).toArray
  }

  private def isIntegerLike(dataType: DataType): Boolean =
    dataType match {
      case ByteType | ShortType | IntegerType | LongType | TimestampType | DateType => true
      case _ => false
    }
}
