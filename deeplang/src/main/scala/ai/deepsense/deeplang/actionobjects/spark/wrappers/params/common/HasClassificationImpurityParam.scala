package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.ClassificationImpurity._
import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.wrappers.spark.ChoiceParameterWrapper

trait HasClassificationImpurityParam extends Params {

  val impurity =
    new ChoiceParameterWrapper[ml.param.Params { val impurity: ml.param.Param[String] }, ClassificationImpurity](
      name = "classification impurity",
      description = Some("The criterion used for information gain calculation."),
      sparkParamGetter = _.impurity
    )

  setDefault(impurity, Gini())

}
