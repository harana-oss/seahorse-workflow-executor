package org.apache.spark.sql.execution.datasources.csv

import org.apache.spark.sql.internal.SQLConf


object MapToCsvOptions {
  def apply(option: Map[String, String], sqlConf: SQLConf) : CSVOptions = {
    new CSVOptions(option)
  }
}
