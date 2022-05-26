package io.deepsense.deeplang.doperables.dataframe

import org.apache.spark.rdd.RDD
import org.apache.spark.sql
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType

import io.deepsense.sparkutils.SparkSQLSession

/** DeepSense DataFrame builder.
  * @param sparkSQLSession
  *   Spark sql context.
  */
case class DataFrameBuilder(sparkSQLSession: SparkSQLSession) {

  def buildDataFrame(schema: StructType, data: RDD[Row]): DataFrame = {
    val dataFrame: sql.DataFrame = sparkSQLSession.createDataFrame(data, schema)
    DataFrame.fromSparkDataFrame(dataFrame)
  }

}

object DataFrameBuilder
