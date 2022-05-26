package ai.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.params.Params
import ai.deepsense.deeplang.params.wrappers.spark.SingleColumnCreatorParamWrapper

trait HasPredictionColumnCreatorParam extends Params {

  val predictionColumn =
    new SingleColumnCreatorParamWrapper[ml.param.Params { val predictionCol: ml.param.Param[String] }](
      name = "prediction column",
      description = Some("The prediction column created during model scoring."),
      sparkParamGetter = _.predictionCol
    )

  setDefault(predictionColumn, "prediction")

}
