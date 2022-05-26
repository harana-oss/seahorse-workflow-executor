package io.deepsense.deeplang.doperations.inout

import io.deepsense.deeplang.params.BooleanParam
import io.deepsense.deeplang.params.Params

trait HasShouldConvertToBooleanParam {
  this: Params =>

  val shouldConvertToBoolean = BooleanParam(
    name = "convert to boolean",
    description = Some("Should columns containing only 0 and 1 be converted to Boolean?")
  )

  setDefault(shouldConvertToBoolean, false)

  def getShouldConvertToBoolean: Boolean = $(shouldConvertToBoolean)

  def setShouldConvertToBoolean(value: Boolean): this.type =
    set(shouldConvertToBoolean, value)

}
