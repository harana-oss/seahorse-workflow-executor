package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import scala.reflect.runtime.universe._

import org.apache.spark.ml.classification.{GBTClassificationModel => SparkGBTClassificationModel}
import org.apache.spark.ml.classification.{GBTClassifier => SparkGBTClassifier}

import ai.deepsense.commons.utils.Logging
import ai.deepsense.deeplang.actionobjects.SparkEstimatorWrapper
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.GBTClassificationModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.VanillaGBTClassificationModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.GBTParams
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasClassificationImpurityParam
import ai.deepsense.deeplang.actionobjects.stringindexingwrapper.StringIndexingEstimatorWrapper
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.choice.Choice
import ai.deepsense.deeplang.parameters.wrappers.spark.ChoiceParameterWrapper
import ai.deepsense.deeplang.utils.TypeUtils

class GBTClassifier private (val vanillaGBTClassifier: VanillaGBTClassifier)
    extends StringIndexingEstimatorWrapper[
      SparkGBTClassificationModel,
      SparkGBTClassifier,
      VanillaGBTClassificationModel,
      GBTClassificationModel
    ](vanillaGBTClassifier) {

  def this() = this(new VanillaGBTClassifier())

}

class VanillaGBTClassifier()
    extends SparkEstimatorWrapper[SparkGBTClassificationModel, SparkGBTClassifier, VanillaGBTClassificationModel]
    with GBTParams
    with HasClassificationImpurityParam
    with Logging {

  import GBTClassifier._

  override lazy val maxIterationsDefault = 10.0

  val estimator = TypeUtils.instanceOfType(typeTag[SparkGBTClassifier])

  val lossType = new ChoiceParameterWrapper[SparkGBTClassifier, LossType](
    name = "loss function",
    description = Some("The loss function which GBT tries to minimize."),
    sparkParamGetter = _.lossType
  )

  setDefault(lossType, Logistic())

  override val params: Array[Parameter[_]] = Array(impurity, lossType, maxBins, maxDepth, maxIterations, minInfoGain,
    minInstancesPerNode, seed, stepSize, subsamplingRate, labelColumn, featuresColumn, predictionColumn)

  override protected def estimatorName: String = classOf[GBTClassifier].getSimpleName

}

object GBTClassifier {

  sealed abstract class LossType(override val name: String) extends Choice {

    override val params: Array[Parameter[_]] = Array()

    override val choiceOrder: List[Class[_ <: Choice]] = List(
      classOf[Logistic]
    )

  }

  case class Logistic() extends LossType("logistic")

}
