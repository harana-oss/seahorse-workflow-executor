package ai.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.params.Params
import ai.deepsense.deeplang.params.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.params.wrappers.spark.SingleColumnSelectorParamWrapper

trait HasItemColumnParam extends Params {

  val itemColumn =
    new SingleColumnSelectorParamWrapper[ml.param.Params { val itemCol: ml.param.Param[String] }](
      name = "item column",
      description = Some("The column for item ids."),
      sparkParamGetter = _.itemCol,
      portIndex = 0
    )

  setDefault(itemColumn, NameSingleColumnSelection("item"))

}
