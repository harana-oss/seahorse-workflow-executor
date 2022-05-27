package ai.deepsense.deeplang.actions.custom

import scala.reflect.runtime.{universe => ru}

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang._
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.Parameter

case class Sink() extends Action1To1[DataFrame, DataFrame] with OperationDocumentation {

  override val id: Id = Sink.id

  override val name: String = "Sink"

  override val description: String = "Custom transformer sink"

  override val since: Version = Version(1, 0, 0)

  override val specificParams: Array[Parameter[_]] = Array()

  @transient
  override lazy val tTagTI_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

  @transient
  override lazy val tTagTO_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

  override protected def execute(dataFrame: DataFrame)(context: ExecutionContext): DataFrame =
    dataFrame

  override protected def inferKnowledge(inputKnowledge: Knowledge[DataFrame])(
      context: InferContext
  ): (Knowledge[DataFrame], InferenceWarnings) =
    (inputKnowledge, InferenceWarnings.empty)

}

object Sink {

  val id: Id = "e652238f-7415-4da6-95c6-ee33808561b2"

}
