
package org.apache.spark.sql.execution.datasources.csv

import org.apache.spark.rdd.RDD


object SparkCsvReader {
  def create(options: CSVOptions): CsvReader = new CsvReader(options)
  def univocityTokenizer(
    rdd: RDD[String],
    header: Array[String],
    firstLine: String,
    options: CSVOptions): RDD[Array[String]] =
    CSVRelation.univocityTokenizer(rdd, firstLine, options)
}
