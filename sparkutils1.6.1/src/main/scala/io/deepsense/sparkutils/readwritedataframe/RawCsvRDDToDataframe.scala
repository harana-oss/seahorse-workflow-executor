package org.apache.spark.sql.execution.datasources.csv

import com.databricks.spark.csv.{CsvRelation, DeepsenseDefaultSource}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql._

import io.deepsense.sparkutils.SparkSQLSession

object RawCsvRDDToDataframe {

  def parse(
      rdd: RDD[String],
      sparkSQLSession: SparkSQLSession,
      options: Map[String, String]): DataFrame = {

    val sqlContext = sparkSQLSession.getSQLContext
    val relation = DeepsenseDefaultSource.createRelation(sqlContext, options, rdd
    ).asInstanceOf[CsvRelation]
    sqlContext.baseRelationToDataFrame(relation)
  }
}
