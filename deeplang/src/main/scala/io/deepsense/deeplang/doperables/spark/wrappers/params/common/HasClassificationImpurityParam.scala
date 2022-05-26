package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import io.deepsense.deeplang.doperables.spark.wrappers.params.common.ClassificationImpurity._
import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.wrappers.spark.ChoiceParamWrapper

trait HasClassificationImpurityParam extends Params {

  val impurity =
    new ChoiceParamWrapper[ml.param.Params { val impurity: ml.param.Param[String] }, ClassificationImpurity](
      name = "classification impurity",
      description = Some("The criterion used for information gain calculation."),
      sparkParamGetter = _.impurity
    )

  setDefault(impurity, Gini())

}
