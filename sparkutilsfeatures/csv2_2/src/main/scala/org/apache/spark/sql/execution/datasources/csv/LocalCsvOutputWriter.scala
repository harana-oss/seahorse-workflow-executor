package org.apache.spark.sql.execution.datasources.csv

import java.io.PrintWriter
import com.univocity.parsers.csv.{CsvWriter, CsvWriterSettings}
import org.apache.spark.sql.types._

/**
  * Heavily based on org.apache.spark.sql.execution.datasources.csv.CsvOutputWriter
  * Instead of writing to Hadoop Text File it writes to local file system
  */
class LocalCsvOutputWriter(
      schema: StructType,
      options: CSVOptions,
      driverPath: String) {

  private val driverFileWriter = new PrintWriter(driverPath)

  private val FLUSH_BATCH_SIZE = 1024L
  private var records: Long = 0L
  private val writerSettings = createWriterSettings(schema, options)
  private val gen = new CsvWriter(driverFileWriter, writerSettings)

  def write(row: Seq[String]): Unit = {
    gen.writeRow(row.toArray)
    records += 1
    if (records % FLUSH_BATCH_SIZE == 0) {
      flush()
    }
  }

  def close(): Unit = {
    flush()
    driverFileWriter.close()
  }

  private def flush(): Unit = {
    gen.flush()
  }

  private def createWriterSettings(schema: StructType, options: CSVOptions): CsvWriterSettings = {
    val writerSettings = options.asWriterSettings
    writerSettings.setHeaderWritingEnabled(options.headerFlag)
    writerSettings.setHeaders(schema.fieldNames: _*)
    writerSettings
  }
}
