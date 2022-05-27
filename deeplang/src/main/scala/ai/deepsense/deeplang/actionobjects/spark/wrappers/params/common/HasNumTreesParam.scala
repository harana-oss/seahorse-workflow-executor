package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.regression.RandomForestRegressor

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper

trait HasNumTreesParam extends Params {

  val numTrees = new IntParameterWrapper[ml.param.Params { val numTrees: ml.param.IntParam }](
    name = "num trees",
    description = Some("The number of trees to train."),
    sparkParamGetter = _.numTrees,
    validator = RangeValidator(begin = 1.0, end = Int.MaxValue, step = Some(1.0))
  )

  setDefault(numTrees, 20.0)

}
