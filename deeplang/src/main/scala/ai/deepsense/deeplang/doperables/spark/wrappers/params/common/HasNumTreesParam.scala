package ai.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.regression.RandomForestRegressor

import ai.deepsense.deeplang.params.Params
import ai.deepsense.deeplang.params.validators.RangeValidator
import ai.deepsense.deeplang.params.wrappers.spark.IntParamWrapper

trait HasNumTreesParam extends Params {

  val numTrees = new IntParamWrapper[ml.param.Params { val numTrees: ml.param.IntParam }](
    name = "num trees",
    description = Some("The number of trees to train."),
    sparkParamGetter = _.numTrees,
    validator = RangeValidator(begin = 1.0, end = Int.MaxValue, step = Some(1.0))
  )

  setDefault(numTrees, 20.0)

}
