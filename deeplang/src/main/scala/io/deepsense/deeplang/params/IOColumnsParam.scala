package io.deepsense.deeplang.params

import io.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.SingleOrMultiColumnChoice
import io.deepsense.deeplang.params.choice.ChoiceParam

case class IOColumnsParam()
  extends ChoiceParam[SingleOrMultiColumnChoice](
    name = "operate on",
    description = Some("The input and output columns for the operation."))
