package io.deepsense.deeplang.doperables.multicolumn

import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleColumnInPlaceChoice
import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.YesInPlaceChoice
import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.choice.ChoiceParam

trait HasSingleInPlaceParam extends Params {
  val singleInPlaceChoice = ChoiceParam[SingleColumnInPlaceChoice](
    name = "output",
    description = Some("Output generation mode.")
  )
  setDefault(singleInPlaceChoice -> YesInPlaceChoice())
}
