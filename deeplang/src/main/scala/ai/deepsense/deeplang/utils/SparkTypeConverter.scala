package ai.deepsense.deeplang.utils

import org.apache.spark.mllib.linalg.SparseVector
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.sql.Row
import org.joda.time.DateTime
import org.joda.time.LocalDate

import ai.deepsense.commons.datetime.DateTimeConverter
import ai.deepsense.commons.utils.DoubleUtils

/** Provides converters from java types returned by Spark. Check @{link Row.get()} for possible types of Any returned by
  * Spark
  */
object SparkTypeConverter {

  val defaultLimitInSeq = 20

  def getColumnAsDouble(columnIndex: Int)(row: Row): Double =
    getOption(columnIndex)(row).map(SparkTypeConverter.sparkAnyToDouble).getOrElse(Double.NaN)

  /** @return
    *   "NULL" for empty values. <p> String representation for non-empty values.
    */
  def getColumnAsString(columnIndex: Int)(row: Row): String =
    getOption(columnIndex)(row).map(SparkTypeConverter.sparkAnyToString).getOrElse("NULL")

  def rowToDoubleVector(row: Row): Vector = {
    val values = (0 until row.size).map { columnIndex =>
      getOption(columnIndex)(row).map(SparkTypeConverter.sparkAnyToDouble).getOrElse(Double.NaN)
    }
    Vectors.dense(values.toArray)
  }

  def getOption(column: Int)(row: Row): Option[Any] = {
    if (row.isNullAt(column))
      None
    else
      Some(row.get(column))
  }

  def cellToString(row: Row, index: Int): Option[String] = {
    if (row.isNullAt(index))
      None
    else {
      val sparkAny = row.get(index)
      Some(sparkAnyToString(sparkAny))
    }
  }

  def cellToDouble(row: Row, column: Int): Option[Double] =
    if (row.isNullAt(column))
      None
    else {
      val sparkAny = row.get(column)
      Some(sparkAnyToDouble(sparkAny))
    }

  def sparkAnyToString(value: Any): String = {
    value match {
      case sparseVector: SparseVector    => sparseVectorToString(sparseVector)
      case vector: Vector                => sparkAnyToString(vector.toArray)
      case array: Array[_]               => sparkAnyToString(array.toSeq)
      case seq: Seq[_]                   => seqToString(seq)
      case (key, tupleValue)             => s"(${sparkAnyToString(key)}, ${sparkAnyToString(tupleValue)})"
      case float: java.lang.Float        => DoubleUtils.double2String(float.toDouble)
      case double: java.lang.Double      => DoubleUtils.double2String(double)
      case decimal: java.math.BigDecimal => decimal.toPlainString
      case timestamp: java.sql.Timestamp => DateTimeConverter.toString(DateTimeConverter.fromMillis(timestamp.getTime))
      case date: java.sql.Date           => DateTimeConverter.toString(DateTimeConverter.fromMillis(date.getTime))
      case string: String                => string
      case other                         => other.toString
    }
  }

  def sparkAnyToDouble(value: Any): Double = {
    value match {
      case bool: java.lang.Boolean       => if (bool) 1d else 0d
      case n: Number                     => n.doubleValue()
      case date: java.sql.Date           => dateToDouble(date)
      case timestamp: java.sql.Timestamp => timestamp.getTime.toDouble
      case other                         => Double.NaN
    }
  }

  private def dateToDouble(date: java.sql.Date): Double = {
    val localDate = new LocalDate(date)
    new DateTime(
      localDate.getYear,
      localDate.getMonthOfYear,
      localDate.getDayOfMonth,
      0,
      0,
      DateTimeConverter.zone
    ).getMillis
  }

  private def seqToString(seq: Seq[_], optionalLimit: Option[Int] = Some(defaultLimitInSeq)): String = {
    val moreValuesMark = "..."
    val itemsToString  = optionalLimit match {
      case None        => seq
      case Some(limit) => if (seq.length > limit) seq.take(limit) ++ Seq(moreValuesMark) else seq
    }
    itemsToString.map(sparkAnyToString).mkString("[", ", ", "]")
  }

  private def sparseVectorToString(sparseVector: SparseVector): String = {
    val size    = sparseVector.size
    val indices = sparseVector.indices.toStream
    val values  = sparseVector.values.toStream
    val pairs   = indices.zip(values)

    s"($size, ${sparkAnyToString(pairs)})"
  }

}
