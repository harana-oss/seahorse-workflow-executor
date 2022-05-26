package ai.deepsense.deeplang.params.exceptions

import ai.deepsense.deeplang.params.selections.IndexRangeColumnSelection

case class IllegalIndexRangeColumnSelectionException(selection: IndexRangeColumnSelection)
    extends ValidationException(
      s"The column selection $selection is invalid. " +
        "All bounds should be set and lower bound should be less or equal upper bound."
    )
