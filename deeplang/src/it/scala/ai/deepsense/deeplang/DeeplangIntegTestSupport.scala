package ai.deepsense.deeplang

import scala.concurrent.Future
import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.scalatest.BeforeAndAfterAll

import ai.deepsense.commons.models.Id
import ai.deepsense.commons.spark.sql.UserDefinedFunctions
import ai.deepsense.deeplang.ActionExecutionDispatcher.Result
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.utils.DataFrameMatchers
import ai.deepsense.sparkutils.SparkSQLSession
import ai.deepsense.sparkutils.spi.SparkSessionInitializer

/** Adds features to facilitate integration testing using Spark */
trait DeeplangIntegTestSupport extends UnitSpec with BeforeAndAfterAll with LocalExecutionContext {

  def executeOperation(op: Action, dfs: DataFrame*): DataFrame =
    op.executeUntyped(dfs.toVector)(executionContext).head.asInstanceOf[DataFrame]

  def createDir(path: String): Unit =
    new java.io.File(path + "/id").getParentFile.mkdirs()

  def createDataFrame[T <: Product: TypeTag: ClassTag](seq: Seq[T]): DataFrame =
    DataFrame.fromSparkDataFrame(sparkSQLSession.createDataFrame(sparkContext.parallelize(seq)))

}

object DeeplangIntegTestSupport extends UnitSpec with DataFrameMatchers {}

class TestUDFRegistrator extends SparkSessionInitializer {

  val myOp = (d: Double) => d.toInt

  override def init(sparkSession: SparkSession): Unit =
    sparkSession.udf.register("myOp", myOp)

}

private class MockedCodeExecutor extends CustomCodeExecutor {

  override def isValid(code: String): Boolean = true

  override def run(workflowId: String, nodeId: String, code: String): Unit = ()

}

class MockedContextualCodeExecutor
    extends ContextualCustomCodeExecutor(new MockedCustomCodeExecutionProvider, Id.randomId, Id.randomId)

private class MockedCustomCodeExecutionProvider
    extends CustomCodeExecutionProvider(
      new MockedCodeExecutor,
      new MockedCodeExecutor,
      new MockedCustomOperationExecutor
    )

private class MockedCustomOperationExecutor extends ActionExecutionDispatcher {

  override def executionStarted(workflowId: Id, nodeId: Id): Future[Result] =
    Future.successful(Right(()))

}
