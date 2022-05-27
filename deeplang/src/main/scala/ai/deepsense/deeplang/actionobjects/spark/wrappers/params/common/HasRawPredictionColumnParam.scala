package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.parameters.wrappers.spark.SingleColumnSelectorParameterWrapper

trait HasRawPredictionColumnParam extends Params {

  val rawPredictionColumn =
    new SingleColumnSelectorParameterWrapper[ml.param.Params { val rawPredictionCol: ml.param.Param[String] }](
      name = "raw prediction column",
      description = Some("The raw prediction (confidence) column."),
      sparkParamGetter = _.rawPredictionCol,
      portIndex = 0
    )

  setDefault(rawPredictionColumn, NameSingleColumnSelection("rawPrediction"))

}
