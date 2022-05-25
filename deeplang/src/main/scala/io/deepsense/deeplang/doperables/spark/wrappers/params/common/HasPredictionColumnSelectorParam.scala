package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection
import io.deepsense.deeplang.params.wrappers.spark.SingleColumnSelectorParamWrapper

trait HasPredictionColumnSelectorParam extends Params {

  val predictionColumn =
    new SingleColumnSelectorParamWrapper[
        ml.param.Params { val predictionCol: ml.param.Param[String] }](
      name = "prediction column",
      description = Some("The prediction column."),
      sparkParamGetter = _.predictionCol,
      portIndex = 0)
  setDefault(predictionColumn, NameSingleColumnSelection("prediction"))
}
