package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection
import io.deepsense.deeplang.params.wrappers.spark.SingleColumnSelectorParamWrapper

trait HasInputColumn extends Params {

  val inputColumn = new SingleColumnSelectorParamWrapper[
      ml.param.Params { val inputCol: ml.param.Param[String] }](
    name = "input column",
    description = Some("The input column name."),
    sparkParamGetter = _.inputCol,
    portIndex = 0)

  def setInputColumn(value: String): this.type = {
    set(inputColumn, NameSingleColumnSelection(value))
  }
}
