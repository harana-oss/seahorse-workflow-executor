package ai.deepsense.deeplang.params.exceptions

case class ParamValueNotProvidedException(name: String) extends ValidationException(s"No value for parameter '$name'")
