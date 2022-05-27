package ai.deepsense.deeplang.actionobjects.spark.wrappers.params

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common._
import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.validators.ArrayLengthValidator
import ai.deepsense.deeplang.parameters.validators.ComplexArrayValidator
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.DoubleArrayParameterWrapper

trait AFTSurvivalRegressionParams extends Params with PredictorParams with HasOptionalQuantilesColumnParam {

  val quantileProbabilities =
    new DoubleArrayParameterWrapper[ml.param.Params { val quantileProbabilities: ml.param.DoubleArrayParam }](
      name = "quantile probabilities",
      description = Some("""Param for quantile probabilities array.
                           |Values of the quantile probabilities array should be in the range (0, 1)
                           |and the array should be non-empty.""".stripMargin),
      sparkParamGetter = _.quantileProbabilities,
      validator = ComplexArrayValidator(
        rangeValidator = RangeValidator(0, 1, beginIncluded = false, endIncluded = false),
        lengthValidator = ArrayLengthValidator.withAtLeast(1)
      )
    )

  setDefault(quantileProbabilities, Array(0.01, 0.05, 0.1, 0.25, 0.5, 0.75, 0.9, 0.95, 0.99))

}
