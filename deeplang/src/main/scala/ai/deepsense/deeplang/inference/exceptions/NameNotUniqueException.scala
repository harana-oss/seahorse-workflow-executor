package ai.deepsense.deeplang.inference.exceptions

import ai.deepsense.deeplang.exceptions.FlowException

case class NameNotUniqueException(name: String) extends FlowException(s"Name '$name' is not unique")
