package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.regression.RandomForestRegressor

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.DoubleParameterWrapper
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper

trait HasMinInfoGainParam extends Params {

  val minInfoGain =
    new DoubleParameterWrapper[ml.param.Params { val minInfoGain: ml.param.DoubleParam }](
      name = "min information gain",
      description = Some("The minimum information gain for a split to be considered at a tree node."),
      sparkParamGetter = _.minInfoGain,
      RangeValidator(0.0, Double.MaxValue)
    )

  setDefault(minInfoGain, 0.0)

}
