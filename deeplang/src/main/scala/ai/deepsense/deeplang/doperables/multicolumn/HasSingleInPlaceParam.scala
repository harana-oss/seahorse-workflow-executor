package ai.deepsense.deeplang.doperables.multicolumn

import ai.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleColumnInPlaceChoice
import ai.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.YesInPlaceChoice
import ai.deepsense.deeplang.params.Params
import ai.deepsense.deeplang.params.choice.ChoiceParam

trait HasSingleInPlaceParam extends Params {

  val singleInPlaceChoice = ChoiceParam[SingleColumnInPlaceChoice](
    name = "output",
    description = Some("Output generation mode.")
  )

  setDefault(singleInPlaceChoice -> YesInPlaceChoice())

}
