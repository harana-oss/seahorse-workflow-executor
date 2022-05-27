package ai.deepsense.deeplang.actions

import scala.reflect.runtime.universe.TypeTag

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.PythonColumnTransformer

class PythonColumnTransformation extends TransformerAsOperation[PythonColumnTransformer] with OperationDocumentation {

  override val id: Id = "9951d301-7eb7-473b-81ad-0f8659619784"

  override val name: String = "Python Column Transformation"

  override val description: String =
    "Executes a custom Python transformation on a column of a DataFrame"

  override lazy val tTagTO_1: TypeTag[PythonColumnTransformer] = typeTag

  override val since: Version = Version(1, 0, 0)

}
