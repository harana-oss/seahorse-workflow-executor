package ai.deepsense.deeplang.params

import ai.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.SingleOrMultiColumnChoice
import ai.deepsense.deeplang.params.choice.ChoiceParam

case class IOColumnsParam()
    extends ChoiceParam[SingleOrMultiColumnChoice](
      name = "operate on",
      description = Some("The input and output columns for the operation.")
    )
