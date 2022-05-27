package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.regression.RandomForestRegressor

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.BooleanParameterWrapper
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper

trait HasCacheNodeIdsParam extends Params {

  val cacheNodeIds =
    new BooleanParameterWrapper[ml.param.Params { val cacheNodeIds: ml.param.BooleanParam }](
      name = "cache node ids",
      description = Some("The caching nodes IDs. Can speed up training of deeper trees."),
      sparkParamGetter = _.cacheNodeIds
    )

  setDefault(cacheNodeIds, false)

}
