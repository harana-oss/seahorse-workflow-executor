package ai.deepsense.deeplang.actions.custom

import scala.reflect.runtime.{universe => ru}

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.Action0To1
import ai.deepsense.deeplang.ExecutionContext

case class Source() extends Action0To1[DataFrame] with OperationDocumentation {

  override val id: Id = Source.id

  override val name: String = "Source"

  override val description: String = "Custom transformer source"

  override val since: Version = Version(1, 0, 0)

  override val specificParams: Array[Parameter[_]] = Array()

  @transient
  override lazy val tTagTO_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

  override protected def execute()(context: ExecutionContext): DataFrame =
    throw new IllegalStateException("should not be executed")

}

object Source {

  val id: Id = "f94b04d7-ec34-42f7-8100-93fe235c89f8"

}
