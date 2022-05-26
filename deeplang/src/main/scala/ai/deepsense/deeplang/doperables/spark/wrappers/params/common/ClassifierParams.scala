package ai.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.params.wrappers.spark.SingleColumnCreatorParamWrapper

trait ClassifierParams extends PredictorParams {

  val rawPredictionColumn =
    new SingleColumnCreatorParamWrapper[ml.param.Params { val rawPredictionCol: ml.param.Param[String] }](
      name = "raw prediction column",
      description = Some("The raw prediction (confidence) column."),
      sparkParamGetter = _.rawPredictionCol
    )

  setDefault(rawPredictionColumn, "rawPrediction")

}
