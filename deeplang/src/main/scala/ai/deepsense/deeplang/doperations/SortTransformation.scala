package ai.deepsense.deeplang.doperations

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.DOperation.Id
import ai.deepsense.deeplang.doperables.SortTransformer
import scala.reflect.runtime.universe.typeTag
import scala.reflect.runtime.universe.TypeTag

import ai.deepsense.deeplang.documentation.OperationDocumentation

class SortTransformation extends TransformerAsOperation[SortTransformer] with OperationDocumentation {

  override val id: Id = "1fa337cc-26f5-4cff-bd91-517777924d66"

  override val name: String = "Sort"

  override val description: String = "Sorts DataFrame by selected columns"

  override def since: Version = Version(1, 4, 0)

  @transient
  override lazy val tTagTO_1: TypeTag[SortTransformer] = typeTag[SortTransformer]

}
