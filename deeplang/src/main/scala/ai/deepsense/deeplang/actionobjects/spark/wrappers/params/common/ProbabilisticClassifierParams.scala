package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.wrappers.spark.SingleColumnCreatorParameterWrapper

trait ProbabilisticClassifierParams extends ClassifierParams {

  val probabilityColumn =
    new SingleColumnCreatorParameterWrapper[ml.param.Params { val probabilityCol: ml.param.Param[String] }](
      name = "probability column",
      description = Some("The column for predicted class conditional probabilities."),
      sparkParamGetter = _.probabilityCol
    )

  setDefault(probabilityColumn, "probability")

}
