package io.deepsense.deeplang.doperations.readwritedataframe.filestorage

import scala.reflect.runtime.{universe => ru}

import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.{DataFrame => SparkDataFrame}

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.inout.OutputFileFormatChoice.Csv
import io.deepsense.deeplang.doperations.inout.InputFileFormatChoice
import io.deepsense.deeplang.doperations.inout.OutputFileFormatChoice
import io.deepsense.deeplang.doperations.readwritedataframe.FilePath
import io.deepsense.deeplang.doperations.readwritedataframe.filestorage.csv.CsvOptions

private[filestorage] object ClusterFiles {

  import CsvOptions._

  def read(path: FilePath, fileFormat: InputFileFormatChoice)(implicit context: ExecutionContext): SparkDataFrame = {
    val clusterPath = path.fullPath
    fileFormat match {
      case csv: InputFileFormatChoice.Csv         => readCsv(clusterPath, csv)
      case json: InputFileFormatChoice.Json       => context.sparkSQLSession.read.json(clusterPath)
      case parquet: InputFileFormatChoice.Parquet => context.sparkSQLSession.read.parquet(clusterPath)
    }
  }

  def write(dataFrame: DataFrame, path: FilePath, fileFormat: OutputFileFormatChoice, saveMode: SaveMode)(implicit
      context: ExecutionContext
  ): Unit = {
    val clusterPath = path.fullPath
    val writer = fileFormat match {
      case (csvChoice: Csv) =>
        val namesIncluded = csvChoice.getNamesIncluded
        dataFrame.sparkDataFrame.write
          .format("com.databricks.spark.csv")
          .options(CsvOptions.map(namesIncluded, csvChoice.getCsvColumnSeparator()))
      case _: OutputFileFormatChoice.Parquet =>
        // TODO: DS-1480 Writing DF in parquet format when column names contain forbidden chars
        dataFrame.sparkDataFrame.write.format("parquet")
      case _: OutputFileFormatChoice.Json =>
        dataFrame.sparkDataFrame.write.format("json")
    }
    writer.mode(saveMode).save(clusterPath)
  }

  private def readCsv(clusterPath: String, csvChoice: InputFileFormatChoice.Csv)(implicit context: ExecutionContext) =
    context.sparkSQLSession.read
      .format("com.databricks.spark.csv")
      .setCsvOptions(csvChoice.getNamesIncluded, csvChoice.getCsvColumnSeparator())
      .load(clusterPath)

}
