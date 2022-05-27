package ai.deepsense.workflowexecutor.executor

import ai.deepsense.commons.BuildInfo
import ai.deepsense.commons.mail.EmailSender
import ai.deepsense.commons.rest.client.NotebooksClientFactory
import ai.deepsense.commons.rest.client.datasources.DatasourceClientFactory
import ai.deepsense.commons.spark.sql.UserDefinedFunctions
import ai.deepsense.commons.utils.Logging
import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang._
import ai.deepsense.deeplang.catalogs.actionobjects.ActionObjectCatalog
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrameBuilder
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import ai.deepsense.sparkutils.SparkSQLSession
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

trait Executor extends Logging {

  def currentVersion: Version =
    Version(BuildInfo.apiVersionMajor, BuildInfo.apiVersionMinor, BuildInfo.apiVersionPatch)

  def createExecutionContext(
      dataFrameStorage: DataFrameStorage,
      executionMode: ExecutionMode,
      notebooksClientFactory: Option[NotebooksClientFactory],
      emailSender: Option[EmailSender],
      datasourceClientFactory: DatasourceClientFactory,
      customCodeExecutionProvider: CustomCodeExecutionProvider,
      sparkContext: SparkContext,
      sparkSQLSession: SparkSQLSession,
      tempPath: String,
      libraryPath: String,
      dOperableCatalog: Option[ActionObjectCatalog] = None
  ): CommonExecutionContext = {

    val operationsCatalog =
      CatalogRecorder.fromSparkContext(sparkContext).catalogs.operations

    val innerWorkflowExecutor = new InnerWorkflowExecutorImpl(new GraphReader(operationsCatalog))

    val inferContext = InferContext(
      DataFrameBuilder(sparkSQLSession),
      CatalogRecorder.fromSparkContext(sparkContext).catalogs,
      datasourceClientFactory.createClient
    )

    CommonExecutionContext(
      sparkContext,
      sparkSQLSession,
      inferContext,
      executionMode,
      FileSystemClientStub(), // temporarily mocked
      tempPath,
      libraryPath,
      innerWorkflowExecutor,
      dataFrameStorage,
      notebooksClientFactory,
      emailSender,
      customCodeExecutionProvider
    )
  }

  def createSparkContext(): SparkContext = {
    val sparkConf = new SparkConf()
    sparkConf
      .setAppName("Seahorse Workflow Executor")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .registerKryoClasses(Array())

    val sparkContext = new SparkContext(sparkConf)
    sparkContext
  }

  def createSparkSQLSession(sparkContext: SparkContext): SparkSQLSession = {
    val sparkSQLSession = new SparkSQLSession(sparkContext)
    sparkSQLSession
  }

}

object Executor extends Executor
