package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection
import io.deepsense.deeplang.params.wrappers.spark.SingleColumnSelectorParamWrapper

trait HasRawPredictionColumnParam extends Params {

  val rawPredictionColumn =
    new SingleColumnSelectorParamWrapper[
        ml.param.Params { val rawPredictionCol: ml.param.Param[String] }](
      name = "raw prediction column",
      description = Some("The raw prediction (confidence) column."),
      sparkParamGetter = _.rawPredictionCol,
      portIndex = 0)
  setDefault(rawPredictionColumn, NameSingleColumnSelection("rawPrediction"))
}
