package io.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.Projector

class Projection extends TransformerAsOperation[Projector] with OperationDocumentation {

  override val id: Id = "9c3225d8-d430-48c0-a46e-fa83909ad054"

  override val name: String = "Projection"

  override val description: String =
    "Projects subset of columns in specified order and with (optional) new column names"

  override lazy val tTagTO_1: TypeTag[Projector] = typeTag

  override val since: Version = Version(1, 2, 0)

}
