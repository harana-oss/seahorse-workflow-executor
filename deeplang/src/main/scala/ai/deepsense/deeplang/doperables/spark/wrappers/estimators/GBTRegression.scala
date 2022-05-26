package ai.deepsense.deeplang.doperables.spark.wrappers.estimators

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.regression.{GBTRegressionModel => SparkGBTRegressionModel}
import org.apache.spark.ml.regression.{GBTRegressor => SparkGBTRegressor}

import ai.deepsense.deeplang.doperables.SparkEstimatorWrapper
import ai.deepsense.deeplang.doperables.spark.wrappers.models.GBTRegressionModel
import ai.deepsense.deeplang.doperables.spark.wrappers.params.GBTParams
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasRegressionImpurityParam
import ai.deepsense.deeplang.params.Param
import ai.deepsense.deeplang.params.choice.Choice
import ai.deepsense.deeplang.params.wrappers.spark.ChoiceParamWrapper

class GBTRegression
    extends SparkEstimatorWrapper[SparkGBTRegressionModel, SparkGBTRegressor, GBTRegressionModel]
    with GBTParams
    with HasRegressionImpurityParam {

  import GBTRegression._

  override lazy val maxIterationsDefault = 20.0

  val lossType = new ChoiceParamWrapper[ml.param.Params { val lossType: ml.param.Param[String] }, LossType](
    name = "loss function",
    description = Some("The loss function which GBT tries to minimize."),
    sparkParamGetter = _.lossType
  )

  setDefault(lossType, Squared())

  override val params: Array[Param[_]] = Array(impurity, lossType, maxBins, maxDepth, maxIterations, minInfoGain,
    minInstancesPerNode, seed, stepSize, subsamplingRate, labelColumn, featuresColumn, predictionColumn)

}

object GBTRegression {

  sealed abstract class LossType(override val name: String) extends Choice {

    override val params: Array[Param[_]] = Array()

    override val choiceOrder: List[Class[_ <: Choice]] = List(
      classOf[Squared],
      classOf[Absolute]
    )

  }

  case class Squared() extends LossType("squared")

  case class Absolute() extends LossType("absolute")

}
