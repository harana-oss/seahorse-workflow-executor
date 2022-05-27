package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.wrappers.spark.SingleColumnCreatorParameterWrapper

trait ClassifierParams extends PredictorParams {

  val rawPredictionColumn =
    new SingleColumnCreatorParameterWrapper[ml.param.Params { val rawPredictionCol: ml.param.Param[String] }](
      name = "raw prediction column",
      description = Some("The raw prediction (confidence) column."),
      sparkParamGetter = _.rawPredictionCol
    )

  setDefault(rawPredictionColumn, "rawPrediction")

}
