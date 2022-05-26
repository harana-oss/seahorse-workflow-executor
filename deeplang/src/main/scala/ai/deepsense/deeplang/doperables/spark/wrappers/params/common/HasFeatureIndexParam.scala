package ai.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.param.IntParam

import ai.deepsense.deeplang.params.Params
import ai.deepsense.deeplang.params.validators.RangeValidator
import ai.deepsense.deeplang.params.wrappers.spark.IntParamWrapper

trait HasFeatureIndexParam extends Params {

  val featureIndex = new IntParamWrapper[ml.param.Params { val featureIndex: IntParam }](
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
