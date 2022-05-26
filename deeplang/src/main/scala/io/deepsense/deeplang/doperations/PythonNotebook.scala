package io.deepsense.deeplang.doperations

import java.io.ByteArrayInputStream

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.reflect.runtime.{universe => ru}
import scala.util.Failure

import io.deepsense.commons.rest.client.NotebookRestClient

case class PythonNotebook() extends Notebook {

  override val id: Id = "e76ca616-0322-47a5-b390-70c9668265dd"

  override val name: String = "Python Notebook"

  override val description: String = "Creates a Python notebook with access to the DataFrame"

  override val since: Version = Version(1, 0, 0)

  override val notebookType: String = "python"

  override protected def execute(dataFrame: DataFrame)(context: ExecutionContext): Unit = {
    context.dataFrameStorage.setInputDataFrame(0, dataFrame.sparkDataFrame)
    headlessExecution(context)
  }

}
