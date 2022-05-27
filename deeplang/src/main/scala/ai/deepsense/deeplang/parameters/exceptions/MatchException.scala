package ai.deepsense.deeplang.parameters.exceptions

import scala.util.matching.Regex

case class MatchException(value: String, regex: Regex)
    extends ValidationException(s"Parameter value `$value` does not match regex `$regex`.")
