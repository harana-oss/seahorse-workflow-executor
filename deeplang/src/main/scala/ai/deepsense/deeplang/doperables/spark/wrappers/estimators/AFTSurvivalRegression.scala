package ai.deepsense.deeplang.doperables.spark.wrappers.estimators

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.regression.{AFTSurvivalRegression => SparkAFTSurvivalRegression}
import org.apache.spark.ml.regression.{AFTSurvivalRegressionModel => SparkAFTSurvivalRegressionModel}

import ai.deepsense.deeplang.doperables.SparkEstimatorWrapper
import ai.deepsense.deeplang.doperables.spark.wrappers.models.AFTSurvivalRegressionModel
import ai.deepsense.deeplang.doperables.spark.wrappers.params.AFTSurvivalRegressionParams
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasFitIntercept
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasLabelColumnParam
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasMaxIterationsParam
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasTolerance
import ai.deepsense.deeplang.params.Param
import ai.deepsense.deeplang.params.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.params.wrappers.spark.SingleColumnSelectorParamWrapper

class AFTSurvivalRegression
    extends SparkEstimatorWrapper[
      SparkAFTSurvivalRegressionModel,
      SparkAFTSurvivalRegression,
      AFTSurvivalRegressionModel
    ]
    with AFTSurvivalRegressionParams
    with HasLabelColumnParam
    with HasMaxIterationsParam
    with HasTolerance
    with HasFitIntercept {

  val censorColumn =
    new SingleColumnSelectorParamWrapper[ml.param.Params { val censorCol: ml.param.Param[String] }](
      name = "censor column",
      description = Some("""Param for censor column name.
                           |The value of this column could be 0 or 1.
                           |If the value is 1, it means the event has occurred i.e. uncensored;
                           |otherwise censored.""".stripMargin),
      sparkParamGetter = _.censorCol,
      portIndex = 0
    )

  setDefault(censorColumn, NameSingleColumnSelection("censor"))

  override val params: Array[Param[_]] = Array(fitIntercept, maxIterations, tolerance, labelColumn, censorColumn,
    featuresColumn, predictionColumn, quantileProbabilities, optionalQuantilesColumn)

}
