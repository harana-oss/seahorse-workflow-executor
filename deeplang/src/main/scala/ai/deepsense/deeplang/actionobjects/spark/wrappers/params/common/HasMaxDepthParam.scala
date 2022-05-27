package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.regression.RandomForestRegressor

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper

trait HasMaxDepthParam extends Params {

  val maxDepth = new IntParameterWrapper[ml.param.Params { val maxDepth: ml.param.IntParam }](
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
