package ai.deepsense.deeplang.doperations.exceptions

import ai.deepsense.deeplang.exceptions.DeepLangException

case class TooManyPossibleTypesException()
    extends DeepLangException(
      "There is too many possible types. " +
        "Parameters can not be fully validated."
    )
