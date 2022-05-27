package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.regression.RandomForestRegressor

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper
import ai.deepsense.deeplang.parameters.wrappers.spark.DoubleParameterWrapper

trait HasMaxMemoryInMBParam extends Params {

  val maxMemoryInMB = new IntParameterWrapper[ml.param.Params { val maxMemoryInMB: ml.param.IntParam }](
    name = "max memory",
    description = Some("Maximum memory in MB allocated to histogram aggregation."),
    sparkParamGetter = _.maxMemoryInMB,
    validator = RangeValidator.positiveIntegers
  )

  setDefault(maxMemoryInMB, 256.0)

}
