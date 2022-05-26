package ai.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.params.Params
import ai.deepsense.deeplang.params.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.params.selections.SingleColumnSelection
import ai.deepsense.deeplang.params.wrappers.spark.SingleColumnSelectorParamWrapper

trait HasFeaturesColumnParam extends Params {

  val featuresColumn =
    new SingleColumnSelectorParamWrapper[ml.param.Params { val featuresCol: ml.param.Param[String] }](
      name = "features column",
      description = Some("The features column for model fitting."),
      sparkParamGetter = _.featuresCol,
      portIndex = 0
    )

  setDefault(featuresColumn, NameSingleColumnSelection("features"))

  def setFeaturesColumn(value: SingleColumnSelection): this.type = set(featuresColumn, value)

}
