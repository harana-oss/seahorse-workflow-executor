package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.selections.{SingleColumnSelection, NameSingleColumnSelection}
import io.deepsense.deeplang.params.wrappers.spark.SingleColumnSelectorParamWrapper

trait HasLabelColumnParam extends Params {

  val labelColumn =
    new SingleColumnSelectorParamWrapper[
        ml.param.Params { val labelCol: ml.param.Param[String] }](
      name = "label column",
      description = Some("The label column for model fitting."),
      sparkParamGetter = _.labelCol,
      portIndex = 0)
  setDefault(labelColumn, NameSingleColumnSelection("label"))

  def setLabelColumn(value: SingleColumnSelection): this.type = set(labelColumn, value)
}
