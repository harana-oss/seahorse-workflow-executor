package io.deepsense.deeplang.doperables.spark.wrappers.evaluators

import org.apache.spark.ml.evaluation.{MulticlassClassificationEvaluator => SparkMulticlassClassificationEvaluator}

import io.deepsense.deeplang.doperables.SparkEvaluatorWrapper
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.HasLabelColumnParam
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.HasPredictionColumnSelectorParam
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.choice.Choice
import io.deepsense.deeplang.params.wrappers.spark.ChoiceParamWrapper

class MulticlassClassificationEvaluator
    extends SparkEvaluatorWrapper[SparkMulticlassClassificationEvaluator]
    with HasPredictionColumnSelectorParam
    with HasLabelColumnParam {

  import MulticlassClassificationEvaluator._

  val metricName = new ChoiceParamWrapper[SparkMulticlassClassificationEvaluator, Metric](
    name = "multiclass metric",
    description = Some("The metric used in evaluation."),
    sparkParamGetter = _.metricName
  )

  setDefault(metricName, F1())

  override val params: Array[Param[_]] = Array(metricName, predictionColumn, labelColumn)

  override def getMetricName: String = $(metricName).name

}

object MulticlassClassificationEvaluator {

  sealed abstract class Metric(override val name: String) extends Choice {

    override val params: Array[Param[_]] = Array()

    override val choiceOrder: List[Class[_ <: Choice]] = List(
      classOf[F1],
      classOf[Precision],
      classOf[Recall],
      classOf[WeightedPrecision],
      classOf[WeightedRecall]
    )

  }

  case class F1() extends Metric("f1")

  case class Precision() extends Metric("precision")

  case class Recall() extends Metric("recall")

  case class WeightedPrecision() extends Metric("weightedPrecision")

  case class WeightedRecall() extends Metric("weightedRecall")

}
