package ai.deepsense.deeplang

import ai.deepsense.deeplang.Action.ReportParam
import ai.deepsense.deeplang.Action.ReportType
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.ParamPair

object ReportTypeDefault {

  def apply(param: Parameter[ReportType]) = ParamPair(param, ReportParam.Extended())

}
