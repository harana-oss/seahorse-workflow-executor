package io.deepsense.deeplang.doperations.custom

import scala.reflect.runtime.{universe => ru}

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.DOperation0To1
import io.deepsense.deeplang.ExecutionContext

case class Source() extends DOperation0To1[DataFrame] with OperationDocumentation {

  override val id: Id = Source.id

  override val name: String = "Source"

  override val description: String = "Custom transformer source"

  override val since: Version = Version(1, 0, 0)

  override val params: Array[Param[_]] = Array()

  @transient
  override lazy val tTagTO_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

  override protected def execute()(context: ExecutionContext): DataFrame =
    throw new IllegalStateException("should not be executed")

}

object Source {

  val id: Id = "f94b04d7-ec34-42f7-8100-93fe235c89f8"

}
