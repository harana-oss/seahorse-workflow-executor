package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.classification.{NaiveBayes => SparkNaiveBayes}
import org.apache.spark.ml.classification.{NaiveBayesModel => SparkNaiveBayesModel}

import io.deepsense.deeplang.doperables.SparkEstimatorWrapper
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.NaiveBayes.ModelType
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.NaiveBayes.Multinomial
import io.deepsense.deeplang.doperables.spark.wrappers.models.NaiveBayesModel
import io.deepsense.deeplang.doperables.spark.wrappers.params.common._
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.choice.Choice
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.ChoiceParamWrapper
import io.deepsense.deeplang.params.wrappers.spark.DoubleParamWrapper

class NaiveBayes
    extends SparkEstimatorWrapper[SparkNaiveBayesModel, SparkNaiveBayes, NaiveBayesModel]
    with ProbabilisticClassifierParams
    with HasLabelColumnParam {

  val smoothing = new DoubleParamWrapper[ml.param.Params { val smoothing: ml.param.DoubleParam }](
    name = "smoothing",
    description = Some("The smoothing parameter."),
    sparkParamGetter = _.smoothing,
    validator = RangeValidator(begin = 0.0, end = Double.MaxValue)
  )

  setDefault(smoothing, 1.0)

  val modelType =
    new ChoiceParamWrapper[ml.param.Params { val modelType: ml.param.Param[String] }, ModelType](
      name = "modelType",
      description = Some("The model type."),
      sparkParamGetter = _.modelType
    )

  setDefault(modelType, Multinomial())

  override val params: Array[Param[_]] =
    Array(smoothing, modelType, labelColumn, featuresColumn, probabilityColumn, rawPredictionColumn, predictionColumn)

}

object NaiveBayes {

  sealed abstract class ModelType(override val name: String) extends Choice {

    override val params: Array[Param[_]] = Array()

    override val choiceOrder: List[Class[_ <: Choice]] = List(
      classOf[Multinomial],
      classOf[Bernoulli]
    )

  }

  case class Multinomial() extends ModelType("multinomial")

  case class Bernoulli() extends ModelType("bernoulli")

}
