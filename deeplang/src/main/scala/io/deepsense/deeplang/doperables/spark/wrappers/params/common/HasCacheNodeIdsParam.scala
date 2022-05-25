package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.regression.RandomForestRegressor

import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.{BooleanParamWrapper, IntParamWrapper}

trait HasCacheNodeIdsParam extends Params {

  val cacheNodeIds =
    new BooleanParamWrapper[ml.param.Params {val cacheNodeIds: ml.param.BooleanParam}](
      name = "cache node ids",
      description = Some("The caching nodes IDs. Can speed up training of deeper trees."),
      sparkParamGetter = _.cacheNodeIds)
  setDefault(cacheNodeIds, false)

}
