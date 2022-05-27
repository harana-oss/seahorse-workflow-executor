package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper

trait HasNumberOfClustersParam extends Params {

  val k = new IntParameterWrapper[ml.param.Params { val k: ml.param.IntParam }](
    name = "k",
    description = Some("The number of clusters to create."),
    sparkParamGetter = _.k,
    validator = RangeValidator(begin = 2.0, end = Int.MaxValue, step = Some(1.0))
  )

  setDefault(k, 2.0)

}
