package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.parameters.wrappers.spark.SingleColumnSelectorParameterWrapper

trait HasUserColumnParam extends Params {

  val userColumn =
    new SingleColumnSelectorParameterWrapper[ml.param.Params { val userCol: ml.param.Param[String] }](
      name = "user column",
      description = Some("The column for user ids."),
      sparkParamGetter = _.userCol,
      portIndex = 0
    )

  setDefault(userColumn, NameSingleColumnSelection("user"))

}
