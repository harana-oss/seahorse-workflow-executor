package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.parameters.wrappers.spark.SingleColumnSelectorParameterWrapper

trait HasItemColumnParam extends Params {

  val itemColumn =
    new SingleColumnSelectorParameterWrapper[ml.param.Params { val itemCol: ml.param.Param[String] }](
      name = "item column",
      description = Some("The column for item ids."),
      sparkParamGetter = _.itemCol,
      portIndex = 0
    )

  setDefault(itemColumn, NameSingleColumnSelection("item"))

}
