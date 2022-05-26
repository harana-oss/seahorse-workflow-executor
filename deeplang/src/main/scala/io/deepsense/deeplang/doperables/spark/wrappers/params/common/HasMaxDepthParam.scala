package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.regression.RandomForestRegressor

import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.IntParamWrapper

trait HasMaxDepthParam extends Params {

  val maxDepth = new IntParamWrapper[ml.param.Params { val maxDepth: ml.param.IntParam }](
    name = "max depth",
    description = Some(
      "The maximum depth of the tree. " +
        "E.g., depth 0 means 1 leaf node; depth 1 means 1 internal node + 2 leaf nodes."
    ),
    sparkParamGetter = _.maxDepth,
    RangeValidator(0, 30, step = Some(1.0))
  )

  setDefault(maxDepth, 5.0)

}
