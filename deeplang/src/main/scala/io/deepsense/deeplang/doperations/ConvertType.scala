package io.deepsense.deeplang.doperations

import scala.reflect.runtime.universe.TypeTag

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.TypeConverter

class ConvertType extends TransformerAsOperation[TypeConverter] with OperationDocumentation  {

  override val id: Id = "04084863-fdda-46fd-b1fe-796c6b5a0967"
  override val name: String = "Convert Type"
  override val description: String =
    "Converts selected columns of a DataFrame to a different type"

  override lazy val tTagTO_1: TypeTag[TypeConverter] = typeTag

  override val since: Version = Version(0, 4, 0)
}
