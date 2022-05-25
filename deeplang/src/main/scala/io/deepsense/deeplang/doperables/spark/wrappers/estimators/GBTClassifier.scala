package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import scala.reflect.runtime.universe._

import org.apache.spark.ml.classification.{GBTClassificationModel => SparkGBTClassificationModel, GBTClassifier => SparkGBTClassifier}

import io.deepsense.commons.utils.Logging
import io.deepsense.deeplang.TypeUtils
import io.deepsense.deeplang.doperables.SparkEstimatorWrapper
import io.deepsense.deeplang.doperables.spark.wrappers.models.{GBTClassificationModel, VanillaGBTClassificationModel}
import io.deepsense.deeplang.doperables.spark.wrappers.params.GBTParams
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.HasClassificationImpurityParam
import io.deepsense.deeplang.doperables.stringindexingwrapper.StringIndexingEstimatorWrapper
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.choice.Choice
import io.deepsense.deeplang.params.wrappers.spark.ChoiceParamWrapper

class GBTClassifier private (val vanillaGBTClassifier: VanillaGBTClassifier)
  extends StringIndexingEstimatorWrapper[
    SparkGBTClassificationModel,
    SparkGBTClassifier,
    VanillaGBTClassificationModel,
    GBTClassificationModel](vanillaGBTClassifier) {

  def this() = this(new VanillaGBTClassifier())
}

class VanillaGBTClassifier()
  extends SparkEstimatorWrapper[
    SparkGBTClassificationModel,
    SparkGBTClassifier,
    VanillaGBTClassificationModel]
  with GBTParams
  with HasClassificationImpurityParam
  with Logging {

  import GBTClassifier._

  override lazy val maxIterationsDefault = 10.0

  val estimator = TypeUtils.instanceOfType(typeTag[SparkGBTClassifier])

  val lossType = new ChoiceParamWrapper[SparkGBTClassifier, LossType](
    name = "loss function",
    description = Some("The loss function which GBT tries to minimize."),
    sparkParamGetter = _.lossType)
  setDefault(lossType, Logistic())

  override val params: Array[Param[_]] = Array(
    impurity,
    lossType,
    maxBins,
    maxDepth,
    maxIterations,
    minInfoGain,
    minInstancesPerNode,
    seed,
    stepSize,
    subsamplingRate,
    labelColumn,
    featuresColumn,
    predictionColumn)

  override protected def estimatorName: String = classOf[GBTClassifier].getSimpleName
}

object GBTClassifier {

  sealed abstract class LossType(override val name: String) extends Choice {

    override val params: Array[Param[_]] = Array()

    override val choiceOrder: List[Class[_ <: Choice]] = List(
      classOf[Logistic]
    )
  }
  case class Logistic() extends LossType("logistic")

}
