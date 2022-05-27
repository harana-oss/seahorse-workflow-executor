package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.selections.SingleColumnSelection
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.parameters.wrappers.spark.SingleColumnSelectorParameterWrapper

trait HasLabelColumnParam extends Params {

  val labelColumn =
    new SingleColumnSelectorParameterWrapper[ml.param.Params { val labelCol: ml.param.Param[String] }](
      name = "label column",
      description = Some("The label column for model fitting."),
      sparkParamGetter = _.labelCol,
      portIndex = 0
    )

  setDefault(labelColumn, NameSingleColumnSelection("label"))

  def setLabelColumn(value: SingleColumnSelection): this.type = set(labelColumn, value)

}
