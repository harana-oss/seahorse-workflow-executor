package io.deepsense.sparkutils


import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag
import scala.reflect.api.Symbols

import akka.actor.ActorSystem
import org.apache.spark.{ml, SparkContext}
import org.apache.spark.ml.classification.{DecisionTreeClassificationModel, GBTClassificationModel, MultilayerPerceptronClassificationModel, RandomForestClassificationModel}
import org.apache.spark.ml.feature.PCAModel
import org.apache.spark.ml.regression.{DecisionTreeRegressionModel, GBTRegressionModel, RandomForestRegressionModel}
import org.apache.spark.ml.util.{MLReader, MLWriter}
import org.apache.spark.mllib.linalg
import org.apache.spark.rdd.RDD
import org.apache.spark.sql._
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.types.StructType

class SparkSQLSession private[sparkutils](private[sparkutils] val sqlContext: SQLContext) {
  def this(sparkContext: SparkContext) = this(new HiveContext(sparkContext))

  def sparkContext: SparkContext = sqlContext.sparkContext
  def createDataFrame(rdd: RDD[Row], schema: StructType): DataFrame = sqlContext.createDataFrame(rdd, schema)
  def createDataFrame[T <: Product : TypeTag : ClassTag](rdd: RDD[T]): DataFrame = sqlContext.createDataFrame(rdd)
  def createDataFrame[A <: Product : TypeTag](data: Seq[A]): DataFrame = sqlContext.createDataFrame(data)
  def udfRegistration: UDFRegistration = sqlContext.udf
  def table(tableName: String): DataFrame = sqlContext.table(tableName)
  def read: DataFrameReader = sqlContext.read
  def sql(text: String): DataFrame = sqlContext.sql(text)
  def dropTempTable(name: String): Unit = sqlContext.dropTempTable(name)
  def newSession(): SparkSQLSession = new SparkSQLSession(sqlContext.newSession())

  // This is for pyexecutor.py
  def getSQLContext = sqlContext
}

object SQL {
  def registerTempTable(dataFrame: DataFrame, name: String): Unit = dataFrame.registerTempTable(name)
  def sparkSQLSession(dataFrame: DataFrame): SparkSQLSession = new SparkSQLSession(dataFrame.sqlContext)
  def union(dataFrame1: DataFrame, dataFrame2: DataFrame): DataFrame = dataFrame1.unionAll(dataFrame2)
  def createEmptySparkSQLSession(): SparkSQLSession = new SparkSQLSession(SQLContext.getOrCreate(null)).newSession()
  def dataFrameToJsonRDD(dataFrame: DataFrame): RDD[String] = dataFrame.toJSON

  object SqlParser {
    def parseExpression(expr: String): Expression = catalyst.SqlParser.parseExpression(expr)
  }

  type ExceptionThrownByInvalidExpression = org.apache.spark.sql.AnalysisException
}

object Linalg {
  type DenseMatrix = linalg.DenseMatrix
  type Vector = linalg.Vector
  val Vectors = linalg.Vectors
  type VectorUDT = linalg.VectorUDT
}

object ML {
  abstract class Transformer extends ml.Transformer {
    final override def transform(dataset: DataFrame): DataFrame = transformDF(dataset)
    def transformDF(dataFrame: DataFrame): DataFrame
  }
  abstract class Model[T <: ml.Model[T]] extends ml.Model[T] {
    final override def transform(dataset: DataFrame): DataFrame = transformDF(dataset)
    def transformDF(dataFrame: DataFrame): DataFrame
  }
  abstract class Estimator[M <: ml.Model[M]] extends ml.Estimator[M] {
    final override def fit(dataset: DataFrame): M = fitDF(dataset)
    def fitDF(dataFrame: DataFrame): M
  }
  abstract class Evaluator extends ml.evaluation.Evaluator {
    final override def evaluate(dataset: DataFrame): Double = evaluateDF(dataset)
    def evaluateDF(dataFrame: DataFrame): Double
  }

  trait MLReaderWithSparkContext { self: MLReader[_] =>
    def sparkContext: SparkContext = sqlContext.sparkContext
  }
  trait MLWriterWithSparkContext { self: MLWriter =>
    def sparkContext: SparkContext = sqlContext.sparkContext
  }

  object ModelLoading {
    def decisionTreeClassification(path: String): Option[DecisionTreeClassificationModel] = None
    def decisionTreeRegression(path: String): Option[DecisionTreeRegressionModel] = None
    def GBTClassification(path: String): Option[GBTClassificationModel] = None
    def GBTRegression(path: String): Option[GBTRegressionModel] = None
    def multilayerPerceptronClassification(path: String): Option[MultilayerPerceptronClassificationModel] = None
    def randomForestClassification(path: String): Option[RandomForestClassificationModel] = None
    def randomForestRegression(path: String): Option[RandomForestRegressionModel] = None
  }

  object ModelParams {
    def numTreesFromRandomForestRegressionModel(rfModel: RandomForestRegressionModel): Int = rfModel.numTrees

    def pcFromPCAModel(pcaModel: PCAModel): Linalg.DenseMatrix = pcaModel.pc
  }
}

object CSV {
  val EscapeQuoteChar = "\""

  // this is something that Spark-CSV writer does.
  // It's compliant with CSV standard, although unnecessary
  def additionalEscapings(separator: String)(row: String) : String = {
    if (row.startsWith(separator)) {
      "\"\"" + row
    } else {
      row
    }
  }
}

object PythonGateway {
  val gatewayServerHasCallBackClient: Boolean = true
}

object TypeUtils {
  def isAbstract(c: Symbols#ClassSymbolApi): Boolean = c.isAbstractClass
}

object AkkaUtils {
  def terminate(as: ActorSystem): Unit = as.shutdown()
  def awaitTermination(as: ActorSystem): Unit = as.awaitTermination()
}
