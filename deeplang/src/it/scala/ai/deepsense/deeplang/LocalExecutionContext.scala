package ai.deepsense.deeplang

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.scalatestplus.mockito.MockitoSugar._
import ai.deepsense.commons.rest.client.datasources.DatasourceClient
import ai.deepsense.commons.rest.client.datasources.DatasourceInMemoryClientFactory
import ai.deepsense.commons.spark.sql.UserDefinedFunctions
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrameBuilder
import ai.deepsense.deeplang.filesystemclients.LocalFileSystemClient
import ai.deepsense.sparkutils.SparkSQLSession

trait LocalExecutionContext {

  implicit protected lazy val executionContext: ExecutionContext = LocalExecutionContext.createExecutionContext()

  implicit protected lazy val sparkContext = LocalExecutionContext.sparkContext

  protected lazy val sparkSQLSession = LocalExecutionContext.sparkSQLSession

  protected lazy val createDataFrame = LocalExecutionContext.createDataFrame _

}

object LocalExecutionContext {

  def createDataFrame(rows: Seq[Row], schema: StructType): DataFrame = {
    val rdd: RDD[Row]  = sparkContext.parallelize(rows)
    val sparkDataFrame = sparkSQLSession.createDataFrame(rdd, schema)
    DataFrame.fromSparkDataFrame(sparkDataFrame)
  }

  lazy val commonExecutionContext = new CommonExecutionContext(
    sparkContext,
    LocalExecutionContext.sparkSQLSession,
    inferContext,
    ExecutionMode.Batch,
    LocalFileSystemClient(),
    "/tmp",
    "/tmp/library",
    mock[InnerWorkflowExecutor],
    mock[DataFrameStorage],
    None,
    None,
    mock[CustomCodeExecutionProvider]
  )

  def createExecutionContext(datasourceClient: DatasourceClient = defaultDatasourceClient) =
    ExecutionContext(
      sparkContext,
      LocalExecutionContext.sparkSQLSession,
      MockedInferContext(
        dataFrameBuilder = DataFrameBuilder(LocalExecutionContext.sparkSQLSession),
        datasourceClient = datasourceClient
      ),
      ExecutionMode.Batch,
      LocalFileSystemClient(),
      "/tmp",
      "/tmp/library",
      mock[InnerWorkflowExecutor],
      mock[ContextualDataFrameStorage],
      None,
      None,
      new MockedContextualCodeExecutor
    )

  private val defaultDatasourceClient: DatasourceClient =
    new DatasourceInMemoryClientFactory(List.empty).createClient

  private def inferContext = MockedInferContext(
    dataFrameBuilder = DataFrameBuilder(LocalExecutionContext.sparkSQLSession)
  )

  // One per JVM
  private lazy val sparkConf: SparkConf = new SparkConf()
    .setMaster("local[4]")
    .setAppName("TestApp")
    .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .registerKryoClasses(Array())

  lazy val sparkContext: SparkContext = new SparkContext(sparkConf)

  lazy val sparkSQLSession: SparkSQLSession = {
    val sqlSession = new SparkSQLSession(sparkContext)
    sqlSession
  }

}
