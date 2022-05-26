package ai.deepsense.deeplang.params.exceptions

case class ParamsEqualException(firstParamName: String, secondParamName: String, value: String)
    extends ValidationException(s"'$firstParamName' is equal to '$secondParamName' (both are equal to '$value').")
