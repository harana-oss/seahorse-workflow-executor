package ai.deepsense.deeplang.actions.inout

import ai.deepsense.deeplang.parameters.BooleanParameter
import ai.deepsense.deeplang.parameters.Params

trait HasShouldConvertToBooleanParam {
  this: Params =>

  val shouldConvertToBoolean = BooleanParameter(
    name = "convert to boolean",
    description = Some("Should columns containing only 0 and 1 be converted to Boolean?")
  )

  setDefault(shouldConvertToBoolean, false)

  def getShouldConvertToBoolean: Boolean = $(shouldConvertToBoolean)

  def setShouldConvertToBoolean(value: Boolean): this.type =
    set(shouldConvertToBoolean, value)

}
