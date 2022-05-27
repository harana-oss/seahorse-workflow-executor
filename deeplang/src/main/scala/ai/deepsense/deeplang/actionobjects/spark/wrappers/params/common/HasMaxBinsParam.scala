package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.regression.RandomForestRegressor

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper
import ai.deepsense.deeplang.parameters.wrappers.spark.LongParameterWrapper

trait HasMaxBinsParam extends Params {

  val maxBins = new IntParameterWrapper[ml.param.Params { val maxBins: ml.param.IntParam }](
    name = "max bins",
    description = Some(
      "The maximum number of bins used for discretizing continuous features " +
        "and for choosing how to split on features at each node. " +
        "More bins give higher granularity. " +
        "Must be >= 2 and >= number of categories in any categorical feature."
    ),
    sparkParamGetter = _.maxBins,
    RangeValidator(2.0, Int.MaxValue, step = Some(1.0))
  )

  setDefault(maxBins, 32.0)

}
