package ai.deepsense.deeplang.readjsondataset

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Encoders, SparkSession}


trait JsonReader {
  def readJsonFromRdd(rddJson: RDD[String], sparkSession: SparkSession) = {
    val dataset = sparkSession.createDataset(rddJson)(Encoders.STRING)
    sparkSession.read.json(dataset)
  }

}
