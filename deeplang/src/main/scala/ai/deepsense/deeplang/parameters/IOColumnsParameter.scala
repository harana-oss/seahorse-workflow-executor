package ai.deepsense.deeplang.parameters

import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.SingleOrMultiColumnChoice
import ai.deepsense.deeplang.parameters.choice.ChoiceParameter

case class IOColumnsParameter()
    extends ChoiceParameter[SingleOrMultiColumnChoice](
      name = "operate on",
      description = Some("The input and output columns for the operation.")
    )
