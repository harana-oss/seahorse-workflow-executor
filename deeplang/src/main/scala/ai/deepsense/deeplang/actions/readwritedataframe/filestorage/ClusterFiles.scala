package ai.deepsense.deeplang.actions.readwritedataframe.filestorage

import scala.reflect.runtime.{universe => ru}

import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.{DataFrame => SparkDataFrame}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.inout.OutputFileFormatChoice.Csv
import ai.deepsense.deeplang.actions.inout.InputFileFormatChoice
import ai.deepsense.deeplang.actions.inout.OutputFileFormatChoice
import ai.deepsense.deeplang.actions.readwritedataframe.FilePath
import ai.deepsense.deeplang.actions.readwritedataframe.filestorage.csv.CsvOptions

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
    val writer      = fileFormat match {
      case (csvChoice: Csv)                  =>
        val namesIncluded = csvChoice.getNamesIncluded
        dataFrame.sparkDataFrame.write
          .format("com.databricks.spark.csv")
          .options(CsvOptions.map(namesIncluded, csvChoice.getCsvColumnSeparator()))
      case _: OutputFileFormatChoice.Parquet =>
        // TODO: DS-1480 Writing DF in parquet format when column names contain forbidden chars
        dataFrame.sparkDataFrame.write.format("parquet")
      case _: OutputFileFormatChoice.Json    =>
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
