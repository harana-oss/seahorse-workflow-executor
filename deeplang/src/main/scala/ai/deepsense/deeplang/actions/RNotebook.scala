package ai.deepsense.deeplang.actions

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.ExecutionContext

case class RNotebook() extends Notebook {

  override val id: Id = "89198bfd-6c86-40de-8238-68f7e0a0b50e"

  override val name: String = "R Notebook"

  override val description: String = "Creates an R notebook with access to the DataFrame"

  override val since: Version = Version(1, 3, 0)

  override val notebookType: String = "r"

  override protected def execute(dataFrame: DataFrame)(context: ExecutionContext): Unit = {
    context.dataFrameStorage.setInputDataFrame(0, dataFrame.sparkDataFrame)
    headlessExecution(context)
  }

}
