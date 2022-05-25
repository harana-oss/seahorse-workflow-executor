package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import io.deepsense.deeplang.params.wrappers.spark.SingleColumnCreatorParamWrapper

trait ProbabilisticClassifierParams extends ClassifierParams {

  val probabilityColumn =
    new SingleColumnCreatorParamWrapper[
        ml.param.Params { val probabilityCol: ml.param.Param[String] }](
      name = "probability column",
      description = Some("The column for predicted class conditional probabilities."),
      sparkParamGetter = _.probabilityCol)
  setDefault(probabilityColumn, "probability")
}
