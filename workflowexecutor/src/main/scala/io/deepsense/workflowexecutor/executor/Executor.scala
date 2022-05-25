package io.deepsense.workflowexecutor.executor

import java.io.File

import org.apache.spark.{SparkConf, SparkContext}

import io.deepsense.commons.BuildInfo
import io.deepsense.commons.mail.EmailSender
import io.deepsense.commons.rest.client.NotebooksClientFactory
import io.deepsense.commons.rest.client.datasources.DatasourceClientFactory
import io.deepsense.commons.spark.sql.UserDefinedFunctions
import io.deepsense.commons.utils.{Logging, Version}
import io.deepsense.deeplang._
import io.deepsense.deeplang.catalogs.CatalogPair
import io.deepsense.deeplang.catalogs.doperable.DOperableCatalog
import io.deepsense.deeplang.doperables.dataframe.DataFrameBuilder
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import io.deepsense.sparkutils.SparkSQLSession

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
      dOperableCatalog: Option[DOperableCatalog] = None): CommonExecutionContext = {

    val CatalogPair(operableCatalog, operationsCatalog) =
      CatalogRecorder.fromSparkContext(sparkContext).catalogs

    val innerWorkflowExecutor = new InnerWorkflowExecutorImpl(
      new GraphReader(operationsCatalog))

    val inferContext = InferContext(
      DataFrameBuilder(sparkSQLSession),
      operableCatalog,
      innerWorkflowExecutor,
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
      customCodeExecutionProvider)
  }

  def createSparkContext(): SparkContext = {
    val sparkConf = new SparkConf()
    sparkConf.setAppName("Seahorse Workflow Executor")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .registerKryoClasses(Array())

    val sparkContext = new SparkContext(sparkConf)
    sparkContext
  }

  def createSparkSQLSession(sparkContext: SparkContext): SparkSQLSession = {
    val sparkSQLSession = new SparkSQLSession(sparkContext)
    UserDefinedFunctions.registerFunctions(sparkSQLSession.udfRegistration)
    sparkSQLSession
  }

}

object Executor extends Executor
