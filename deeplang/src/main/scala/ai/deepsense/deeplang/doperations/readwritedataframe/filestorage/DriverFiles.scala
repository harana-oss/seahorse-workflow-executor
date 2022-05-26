package ai.deepsense.deeplang.doperations.readwritedataframe.filestorage

import java.io.File
import java.io.IOException
import java.io.PrintWriter

import scala.io.Source

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.execution.datasources.csv.DataframeToDriverCsvFileWriter
import org.apache.spark.sql.execution.datasources.csv.RawCsvRDDToDataframe
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Encoders
import org.apache.spark.sql.Row
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.{DataFrame => SparkDataFrame}
import ai.deepsense.commons.resources.ManagedResource
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import ai.deepsense.deeplang.doperations.inout.InputFileFormatChoice
import ai.deepsense.deeplang.doperations.inout.OutputFileFormatChoice
import ai.deepsense.deeplang.doperations.readwritedataframe.filestorage.csv.CsvOptions
import ai.deepsense.deeplang.doperations.readwritedataframe.FilePath
import ai.deepsense.deeplang.doperations.readwritedataframe.FileScheme
import ai.deepsense.deeplang.readjsondataset.JsonReader
import ai.deepsense.sparkutils.SQL

object DriverFiles extends JsonReader {

  def read(driverPath: String, fileFormat: InputFileFormatChoice)(implicit context: ExecutionContext): SparkDataFrame =
    fileFormat match {
      case csv: InputFileFormatChoice.Csv         => readCsv(driverPath, csv)
      case json: InputFileFormatChoice.Json       => readJson(driverPath)
      case parquet: InputFileFormatChoice.Parquet => throw ParquetNotSupported
    }

  def write(dataFrame: DataFrame, path: FilePath, fileFormat: OutputFileFormatChoice, saveMode: SaveMode)(implicit
      context: ExecutionContext
  ): Unit = {
    path.verifyScheme(FileScheme.File)
    if (saveMode == SaveMode.ErrorIfExists && new File(path.pathWithoutScheme).exists())
      throw new IOException(s"Output file ${path.fullPath} already exists")
    fileFormat match {
      case csv: OutputFileFormatChoice.Csv         => writeCsv(path, csv, dataFrame)
      case json: OutputFileFormatChoice.Json       => writeJson(path, dataFrame)
      case parquet: OutputFileFormatChoice.Parquet => throw ParquetNotSupported
    }
  }

  private def readCsv(driverPath: String, csvChoice: InputFileFormatChoice.Csv)(implicit
      context: ExecutionContext
  ): SparkDataFrame = {
    val params       = CsvOptions.map(csvChoice.getNamesIncluded, csvChoice.getCsvColumnSeparator())
    val lines        = Source.fromFile(driverPath).getLines().toStream
    val fileLinesRdd = context.sparkContext.parallelize(lines)

    RawCsvRDDToDataframe.parse(fileLinesRdd, context.sparkSQLSession.sparkSession, params)
  }

  private def readJson(driverPath: String)(implicit context: ExecutionContext) = {
    val lines        = Source.fromFile(driverPath).getLines().toStream
    val fileLinesRdd = context.sparkContext.parallelize(lines)
    val sparkSession = context.sparkSQLSession.sparkSession
    readJsonFromRdd(fileLinesRdd, sparkSession)
  }

  private def writeCsv(path: FilePath, csvChoice: OutputFileFormatChoice.Csv, dataFrame: DataFrame)(implicit
      context: ExecutionContext
  ): Unit = {
    val params = CsvOptions.map(csvChoice.getNamesIncluded, csvChoice.getCsvColumnSeparator())

    DataframeToDriverCsvFileWriter.write(
      dataFrame.sparkDataFrame,
      params,
      dataFrame.schema.get,
      path.pathWithoutScheme,
      context.sparkSQLSession.sparkSession
    )
  }

  private def writeJson(path: FilePath, dataFrame: DataFrame)(implicit context: ExecutionContext): Unit = {
    val rawJsonLines: RDD[String] = SQL.dataFrameToJsonRDD(dataFrame.sparkDataFrame)
    writeRddToDriverFile(path.pathWithoutScheme, rawJsonLines)
  }

  private def writeRddToDriverFile(driverPath: String, lines: RDD[String]): Unit = {
    val recordSeparator = System.getProperty("line.separator", "\n")
    ManagedResource(new PrintWriter(driverPath)) { writer =>
      lines.collect().foreach(line => writer.write(line + recordSeparator))
    }
  }

}
