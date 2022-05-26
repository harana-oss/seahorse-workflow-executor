package ai.deepsense.deeplang

import ai.deepsense.deeplang.DOperation.ReportParam
import ai.deepsense.deeplang.DOperation.ReportType
import ai.deepsense.deeplang.params.Param
import ai.deepsense.deeplang.params.ParamPair

object ReportTypeDefault {

  def apply(param: Param[ReportType]) = ParamPair(param, ReportParam.Extended())

}
