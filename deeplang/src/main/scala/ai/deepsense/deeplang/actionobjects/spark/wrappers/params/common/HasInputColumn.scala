package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.parameters.wrappers.spark.SingleColumnSelectorParameterWrapper

trait HasInputColumn extends Params {

  val inputColumn = new SingleColumnSelectorParameterWrapper[ml.param.Params { val inputCol: ml.param.Param[String] }](
    name = "input column",
    description = Some("The input column name."),
    sparkParamGetter = _.inputCol,
    portIndex = 0
  )

  def setInputColumn(value: String): this.type =
    set(inputColumn, NameSingleColumnSelection(value))

}
