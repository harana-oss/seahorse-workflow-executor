package ai.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.params.Params
import ai.deepsense.deeplang.params.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.params.wrappers.spark.SingleColumnSelectorParamWrapper

trait HasUserColumnParam extends Params {

  val userColumn =
    new SingleColumnSelectorParamWrapper[ml.param.Params { val userCol: ml.param.Param[String] }](
      name = "user column",
      description = Some("The column for user ids."),
      sparkParamGetter = _.userCol,
      portIndex = 0
    )

  setDefault(userColumn, NameSingleColumnSelection("user"))

}
