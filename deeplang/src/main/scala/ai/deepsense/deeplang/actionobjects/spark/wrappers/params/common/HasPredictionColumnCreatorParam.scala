package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.wrappers.spark.SingleColumnCreatorParameterWrapper

trait HasPredictionColumnCreatorParam extends Params {

  val predictionColumn =
    new SingleColumnCreatorParameterWrapper[ml.param.Params { val predictionCol: ml.param.Param[String] }](
      name = "prediction column",
      description = Some("The prediction column created during model scoring."),
      sparkParamGetter = _.predictionCol
    )

  setDefault(predictionColumn, "prediction")

}
