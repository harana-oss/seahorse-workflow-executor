package ai.deepsense.deeplang.actionobjects.multicolumn

import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleColumnInPlaceChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.YesInPlaceChoice
import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.choice.ChoiceParameter

trait HasSingleInPlaceParam extends Params {

  val singleInPlaceChoice = ChoiceParameter[SingleColumnInPlaceChoice](
    name = "output",
    description = Some("Output generation mode.")
  )

  setDefault(singleInPlaceChoice -> YesInPlaceChoice())

}
