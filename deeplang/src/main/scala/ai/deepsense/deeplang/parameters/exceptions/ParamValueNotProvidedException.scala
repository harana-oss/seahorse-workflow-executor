package ai.deepsense.deeplang.parameters.exceptions

case class ParamValueNotProvidedException(name: String) extends ValidationException(s"No value for parameter '$name'")
