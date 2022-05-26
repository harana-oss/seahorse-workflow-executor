package org.apache.spark.sql.execution.datasources.csv

import org.apache.spark.rdd.RDD
import org.apache.spark.sql._
import org.apache.spark.sql.execution.LogicalRDD
import org.apache.spark.sql.types.{StructType, _}

object SparkCsvReader {
  def create(options: CSVOptions): LineCsvReader = new LineCsvReader(options)
  def univocityTokenizer(
      rdd: RDD[String],
      header: Array[String],
      firstLine: String,
      options: CSVOptions): RDD[Array[String]] =
    CSVRelation.univocityTokenizer(rdd, header, firstLine, options)
}
