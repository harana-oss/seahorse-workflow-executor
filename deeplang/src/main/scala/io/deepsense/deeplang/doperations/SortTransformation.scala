package io.deepsense.deeplang.doperations
import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.doperables.SortTransformer
import scala.reflect.runtime.universe.{typeTag, TypeTag}

import io.deepsense.deeplang.documentation.OperationDocumentation

class SortTransformation extends TransformerAsOperation[SortTransformer] with OperationDocumentation {
  override val id: Id = "1fa337cc-26f5-4cff-bd91-517777924d66"
  override val name: String = "Sort"
  override val description: String = "Sorts DataFrame by selected columns"

  override def since: Version = Version(1, 4, 0)

  @transient
  override lazy val tTagTO_1: TypeTag[SortTransformer] = typeTag[SortTransformer]
}
