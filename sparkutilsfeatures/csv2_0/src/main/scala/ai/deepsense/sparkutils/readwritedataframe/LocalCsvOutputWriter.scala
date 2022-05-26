package org.apache.spark.sql.execution.datasources.csv

import java.io.PrintWriter

import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.types._


/**
  * Heavily based on org.apache.spark.sql.execution.datasources.csv.CsvOutputWriter
  * Instead of writing to Hadoop Text File it writes to local file system
  */
class LocalCsvOutputWriter(
      dataSchema: StructType,
      params: CSVOptions,
      driverPath: String) {

  private val driverFileWriter = new PrintWriter(driverPath)

  private val FLUSH_BATCH_SIZE = 1024L
  private var records: Long = 0L
  private val csvWriter = new LineCsvWriter(params, dataSchema.fieldNames.toSeq)

  def write(row: Seq[String]): Unit = {
    csvWriter.writeRow(row, records == 0L && params.headerFlag)
    records += 1
    if (records % FLUSH_BATCH_SIZE == 0) {
      flush()
    }
  }

  private def flush(): Unit = {
    val lines = csvWriter.flush()
    if (lines.nonEmpty) {
      driverFileWriter.write(lines)
    }
  }

  def close(): Unit = {
    flush()
    driverFileWriter.close()
  }
}
