package io.deepsense.deeplang.doperables.multicolumn

import io.deepsense.deeplang.params.{Param, Params}

/**
 * Specific params are transformation params that do not describe
 * what data will be transformed but how the transformation will
 * behave. Specific params do NOT include params that define
 * input or output columns.
 */
trait HasSpecificParams {
  self: Params =>

  protected def getSpecificParams: Array[Param[_]]
}
