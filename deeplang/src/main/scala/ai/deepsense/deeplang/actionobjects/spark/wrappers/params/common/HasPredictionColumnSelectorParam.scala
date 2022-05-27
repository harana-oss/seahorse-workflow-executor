package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.parameters.wrappers.spark.SingleColumnSelectorParameterWrapper

trait HasPredictionColumnSelectorParam extends Params {

  val predictionColumn =
    new SingleColumnSelectorParameterWrapper[ml.param.Params { val predictionCol: ml.param.Param[String] }](
      name = "prediction column",
      description = Some("The prediction column."),
      sparkParamGetter = _.predictionCol,
      portIndex = 0
    )

  setDefault(predictionColumn, NameSingleColumnSelection("prediction"))

}
