package ai.deepsense.deeplang.actionobjects.spark.wrappers.evaluators

import org.apache.spark.ml.evaluation.{MulticlassClassificationEvaluator => SparkMulticlassClassificationEvaluator}

import ai.deepsense.deeplang.actionobjects.SparkEvaluatorWrapper
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasLabelColumnParam
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasPredictionColumnSelectorParam
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.choice.Choice
import ai.deepsense.deeplang.parameters.wrappers.spark.ChoiceParameterWrapper

class MulticlassClassificationEvaluator
    extends SparkEvaluatorWrapper[SparkMulticlassClassificationEvaluator]
    with HasPredictionColumnSelectorParam
    with HasLabelColumnParam {

  import MulticlassClassificationEvaluator._

  val metricName = new ChoiceParameterWrapper[SparkMulticlassClassificationEvaluator, Metric](
    name = "multiclass metric",
    description = Some("The metric used in evaluation."),
    sparkParamGetter = _.metricName
  )

  setDefault(metricName, F1())

  override val params: Array[Parameter[_]] = Array(metricName, predictionColumn, labelColumn)

  override def getMetricName: String = $(metricName).name

}

object MulticlassClassificationEvaluator {

  sealed abstract class Metric(override val name: String) extends Choice {

    override val params: Array[Parameter[_]] = Array()

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
