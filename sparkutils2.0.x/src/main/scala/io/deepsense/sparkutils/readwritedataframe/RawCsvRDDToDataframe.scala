package org.apache.spark.sql.execution.datasources.csv

import org.apache.spark.rdd.RDD
import org.apache.spark.sql._
import org.apache.spark.sql.execution.LogicalRDD
import org.apache.spark.sql.types.{StructType, _}

import io.deepsense.sparkutils.SparkSQLSession

/**
  * Heavily based on org.apache.spark.sql.execution.datasources.csv.CSVFileFormat
  */
object RawCsvRDDToDataframe {

  def parse(
      rdd: RDD[String],
      sparkSQLSession: SparkSQLSession,
      options: Map[String, String]): DataFrame = {
    val csvOptions = new CSVOptions(options)
    val lineCsvReader = new LineCsvReader(csvOptions)
    val firstLine = findFirstLine(csvOptions, rdd)
    val firstRow = lineCsvReader.parseLine(firstLine)

    val header = if (csvOptions.headerFlag) {
      firstRow.zipWithIndex.map { case (value, index) =>
        if (value == null || value.isEmpty || value == csvOptions.nullValue) s"_c$index" else value
      }
    } else {
      firstRow.zipWithIndex.map { case (value, index) => s"_c$index" }
    }

    val parsedRdd = tokenRdd(rdd, csvOptions, header)

    // TODO Migrate to Spark's schema inferencer eventually
    // val schema = CSVInferSchema.infer(parsedRdd, header, csvOptions)
    val schema = {
      val schemaFields = header.map { fieldName =>
        StructField(fieldName.toString, StringType, nullable = true)
      }
      StructType(schemaFields)
    }

    val ignoreMalformedRows = 0
    val internalRows = parsedRdd.flatMap { row =>
      val parser = CSVRelation.csvParser(schema, header, csvOptions)
      parser(row, ignoreMalformedRows)
    }

    val sparkSession = sparkSQLSession.getSparkSession
    Dataset.ofRows(
      sparkSession,
      LogicalRDD(
        schema.toAttributes,
        internalRows)(sparkSession))
  }

  private def tokenRdd(
      rdd: RDD[String],
      options: CSVOptions,
      header: Array[String]): RDD[Array[String]] = {
    // Make sure firstLine is materialized before sending to executors
    val firstLine = if (options.headerFlag) findFirstLine(options, rdd) else null
    CSVRelation.univocityTokenizer(rdd, header, firstLine, options)
  }

  private def findFirstLine(options: CSVOptions, rdd: RDD[String]): String = {
    if (options.isCommentSet) {
      val comment = options.comment.toString
      rdd.filter { line =>
        line.trim.nonEmpty && !line.startsWith(comment)
      }.first()
    } else {
      rdd.filter { line =>
        line.trim.nonEmpty
      }.first()
    }
  }

}
