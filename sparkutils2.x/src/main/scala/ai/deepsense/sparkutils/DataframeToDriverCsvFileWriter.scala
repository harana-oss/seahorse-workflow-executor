package org.apache.spark.sql.execution.datasources.csv

import ai.deepsense.sparkutils.readwritedataframe.ManagedResource
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, SparkSession}

object DataframeToDriverCsvFileWriter {

  def write(
    dataFrame: DataFrame,
    options: Map[String, String],
    dataSchema: StructType,
    pathWithoutScheme: String,
    sparkSession: SparkSession): Unit = {
    val data = dataFrame.rdd.collect()
    val params = MapToCsvOptions(options, sparkSession.sessionState.conf)
    ManagedResource(
      new LocalCsvOutputWriter(dataSchema, params, pathWithoutScheme)
    ) { writer =>
      data.foreach(row => {
        writer.write(row.toSeq.map(_.asInstanceOf[String]))
      })
    }
  }

}
