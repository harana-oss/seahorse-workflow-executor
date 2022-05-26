package io.deepsense.deeplang.doperables.dataframe.report

import org.apache.spark.mllib.stat.MultivariateStatisticalSummary
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.Row

import io.deepsense.commons.types.ColumnType
import io.deepsense.commons.types.SparkConversions
import io.deepsense.deeplang.doperables.dataframe.report.distribution.DistributionCalculator
import io.deepsense.deeplang.doperables.dataframe.report.distribution.NoDistributionReasons
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.doperables.report.ReportUtils
import io.deepsense.deeplang.utils.SparkTypeConverter
import io.deepsense.reportlib.model._

object DataFrameReportGenerator {

  val ReportContentName: String = "DataFrame Report"

  val DataSampleTableName = "Data Sample"

  val DataSchemaTableName = "Column Names and Types"

  val DataFrameSizeTableName = "DataFrame Size"

  val MaxRowsNumberInReport = 20

  val ColumnNumberToGenerateSimplerReportThreshold = 20

  val StringPreviewMaxLength = 300

  def report(sparkDataFrame: org.apache.spark.sql.DataFrame): Report = {
    val columnsCount = sparkDataFrame.schema.length
    if (columnsCount >= DataFrameReportGenerator.ColumnNumberToGenerateSimplerReportThreshold)
      simplifiedReport(sparkDataFrame)
    else
      fullReport(sparkDataFrame)
  }

  private def fullReport(sparkDataFrame: DataFrame): Report = {
    val multivarStats = calculateMultiColStats(sparkDataFrame)
    val distributions =
      DistributionCalculator.distributionByColumn(sparkDataFrame, multivarStats)
    val tables = Seq(
      sampleTable(sparkDataFrame),
      sizeTable(sparkDataFrame.schema, multivarStats.count)
    )
    Report(ReportContent(ReportContentName, ReportType.DataFrameFull, tables, distributions))
  }

  private def calculateMultiColStats(sparkDataFrame: org.apache.spark.sql.DataFrame): MultivariateStatisticalSummary = {
    val data = sparkDataFrame.rdd.map(SparkTypeConverter.rowToDoubleVector)
    Statistics.colStats(data)
  }

  private def simplifiedReport(sparkDataFrame: DataFrame): Report = {
    val tables = Seq(sizeTable(sparkDataFrame.schema, sparkDataFrame.count()), schemaTable(sparkDataFrame.schema))
    Report(
      ReportContent(
        ReportContentName,
        ReportType.DataFrameSimplified,
        tables,
        noDistributionsForSimplifiedReport(sparkDataFrame.schema)
      )
    )
  }

  private def noDistributionsForSimplifiedReport(schema: StructType): Map[String, Distribution] = {
    for (field <- schema.fields) yield field.name -> NoDistribution(field.name, NoDistributionReasons.SimplifiedReport)
  }.toMap

  private def schemaTable(schema: StructType): Table = {
    val values = schema.fields.zipWithIndex.map { case (field, index) =>
      val columnName = field.name
      val columnType = field.dataType.simpleString
      List(Some(index.toString), Some(columnName), Some(columnType))
    }.toList

    Table(
      DataFrameReportGenerator.DataSchemaTableName,
      s"Preview of columns and their types in dataset",
      Some(List("Column index", "Column name", "Column type")),
      List(ColumnType.numeric, ColumnType.string, ColumnType.string),
      None,
      values
    )
  }

  private def sampleTable(sparkDataFrame: org.apache.spark.sql.DataFrame): Table = {
    val columnsNames: List[String] = sparkDataFrame.schema.fieldNames.toList
    val columnsNumber              = columnsNames.size
    val rows: Array[Row]           = sparkDataFrame.take(DataFrameReportGenerator.MaxRowsNumberInReport)
    val values: List[List[Option[String]]] = rows
      .map(row =>
        (0 until columnsNumber).map { column =>
          SparkTypeConverter
            .cellToString(row, column)
            .map(ReportUtils.shortenLongStrings(_, StringPreviewMaxLength))
        }.toList
      )
      .toList
    val columnTypes: List[ColumnType.ColumnType] =
      sparkDataFrame.schema.map(field => SparkConversions.sparkColumnTypeToColumnType(field.dataType)).toList
    Table(
      DataFrameReportGenerator.DataSampleTableName,
      s"${DataFrameReportGenerator.DataSampleTableName}. " +
        s"Randomly selected ${rows.length} rows",
      Some(columnsNames),
      columnTypes,
      None,
      values
    )
  }

  private def sizeTable(schema: StructType, rowsCount: Long): Table = {
    val columnsCount = schema.length
    Table(
      DataFrameReportGenerator.DataFrameSizeTableName,
      s"${DataFrameReportGenerator.DataFrameSizeTableName}. " +
        s"Number of columns and number of rows in the DataFrame.",
      Some(List("Number of columns", "Number of rows")),
      List(ColumnType.numeric, ColumnType.numeric),
      None,
      List(List(Some(columnsCount.toString), Some(rowsCount.toString)))
    )
  }

}
