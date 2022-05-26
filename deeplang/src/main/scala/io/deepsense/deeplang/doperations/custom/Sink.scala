package io.deepsense.deeplang.doperations.custom

import scala.reflect.runtime.{universe => ru}

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang._
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.inference.InferenceWarnings
import io.deepsense.deeplang.params.Param

case class Sink() extends DOperation1To1[DataFrame, DataFrame] with OperationDocumentation {

  override val id: Id = Sink.id

  override val name: String = "Sink"

  override val description: String = "Custom transformer sink"

  override val since: Version = Version(1, 0, 0)

  override val params: Array[Param[_]] = Array()

  @transient
  override lazy val tTagTI_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

  @transient
  override lazy val tTagTO_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

  override protected def execute(dataFrame: DataFrame)(context: ExecutionContext): DataFrame =
    dataFrame

  override protected def inferKnowledge(inputKnowledge: DKnowledge[DataFrame])(
      context: InferContext
  ): (DKnowledge[DataFrame], InferenceWarnings) =
    (inputKnowledge, InferenceWarnings.empty)

}

object Sink {

  val id: Id = "e652238f-7415-4da6-95c6-ee33808561b2"

}
