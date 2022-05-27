package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.parameters.selections.SingleColumnSelection
import ai.deepsense.deeplang.parameters.wrappers.spark.SingleColumnSelectorParameterWrapper

trait HasFeaturesColumnParam extends Params {

  val featuresColumn =
    new SingleColumnSelectorParameterWrapper[ml.param.Params { val featuresCol: ml.param.Param[String] }](
      name = "features column",
      description = Some("The features column for model fitting."),
      sparkParamGetter = _.featuresCol,
      portIndex = 0
    )

  setDefault(featuresColumn, NameSingleColumnSelection("features"))

  def setFeaturesColumn(value: SingleColumnSelection): this.type = set(featuresColumn, value)

}
