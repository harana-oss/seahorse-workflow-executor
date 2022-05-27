package ai.deepsense.deeplang.actionobjects.multicolumn

import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.Params

/** Specific params are transformation params that do not describe what data will be transformed but how the
  * transformation will behave. Specific params do NOT include params that define input or output columns.
  */
trait HasSpecificParams {
  self: Params =>

  protected def getSpecificParams: Array[Parameter[_]]

}
