package org.apache.spark.sql.execution.datasources.csv

import java.io.PrintWriter

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types._

import io.deepsense.sparkutils.readwritedataframe.{DataframeToRawCsvRDD, ManagedResource}

object DataframeToDriverCsvFileWriter {

  def write(
      dataFrame: DataFrame,
      options: Map[String, String],
      dataSchema: StructType,
      pathWithoutScheme: String): Unit = {
    val rawCsvLines = DataframeToRawCsvRDD(dataFrame, options)(dataFrame.sqlContext.sparkContext)
    writeRddToDriverFile(pathWithoutScheme, rawCsvLines)
  }

  // TODO extract to commons from DriverFiles
  private def writeRddToDriverFile(driverPath: String, lines: RDD[String]): Unit = {
    val recordSeparator = System.getProperty("line.separator", "\n")
    ManagedResource(new PrintWriter(driverPath)) { writer =>
      lines.collect().foreach(line => writer.write(line + recordSeparator))
    }
  }

}
