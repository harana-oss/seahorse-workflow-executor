package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.param.IntParam

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper

trait HasFeatureIndexParam extends Params {

  val featureIndex = new IntParameterWrapper[ml.param.Params { val featureIndex: IntParam }](
    name = "feature index",
    description = Some(
      "The index of the feature if features column is a vector column, " +
        "no effect otherwise."
    ),
    sparkParamGetter = _.featureIndex,
    validator = RangeValidator.positiveIntegers
  )

  setDefault(featureIndex, 0.0)

}
