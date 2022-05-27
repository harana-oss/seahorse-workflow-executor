package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.classification.{NaiveBayes => SparkNaiveBayes}
import org.apache.spark.ml.classification.{NaiveBayesModel => SparkNaiveBayesModel}

import ai.deepsense.deeplang.actionobjects.SparkEstimatorWrapper
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.NaiveBayes.ModelType
import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.NaiveBayes.Multinomial
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.NaiveBayesModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common._
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.choice.Choice
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.ChoiceParameterWrapper
import ai.deepsense.deeplang.parameters.wrappers.spark.DoubleParameterWrapper

class NaiveBayes
    extends SparkEstimatorWrapper[SparkNaiveBayesModel, SparkNaiveBayes, NaiveBayesModel]
    with ProbabilisticClassifierParams
    with HasLabelColumnParam {

  val smoothing = new DoubleParameterWrapper[ml.param.Params { val smoothing: ml.param.DoubleParam }](
    name = "smoothing",
    description = Some("The smoothing parameter."),
    sparkParamGetter = _.smoothing,
    validator = RangeValidator(begin = 0.0, end = Double.MaxValue)
  )

  setDefault(smoothing, 1.0)

  val modelType =
    new ChoiceParameterWrapper[ml.param.Params { val modelType: ml.param.Param[String] }, ModelType](
      name = "modelType",
      description = Some("The model type."),
      sparkParamGetter = _.modelType
    )

  setDefault(modelType, Multinomial())

  override val params: Array[Parameter[_]] =
    Array(smoothing, modelType, labelColumn, featuresColumn, probabilityColumn, rawPredictionColumn, predictionColumn)

}

object NaiveBayes {

  sealed abstract class ModelType(override val name: String) extends Choice {

    override val params: Array[Parameter[_]] = Array()

    override val choiceOrder: List[Class[_ <: Choice]] = List(
      classOf[Multinomial],
      classOf[Bernoulli]
    )

  }

  case class Multinomial() extends ModelType("multinomial")

  case class Bernoulli() extends ModelType("bernoulli")

}
