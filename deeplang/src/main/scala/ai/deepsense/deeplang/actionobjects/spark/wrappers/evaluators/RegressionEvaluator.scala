package ai.deepsense.deeplang.actionobjects.spark.wrappers.evaluators

import org.apache.spark.ml.evaluation.{RegressionEvaluator => SparkRegressionEvaluator}

import ai.deepsense.deeplang.actionobjects.SparkEvaluatorWrapper
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasLabelColumnParam
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasPredictionColumnSelectorParam
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.choice.Choice
import ai.deepsense.deeplang.parameters.wrappers.spark.ChoiceParameterWrapper

class RegressionEvaluator
    extends SparkEvaluatorWrapper[SparkRegressionEvaluator]
    with HasPredictionColumnSelectorParam
    with HasLabelColumnParam {

  import RegressionEvaluator._

  val metricName = new ChoiceParameterWrapper[SparkRegressionEvaluator, Metric](
    name = "regression metric",
    description = Some("The metric used in evaluation."),
    sparkParamGetter = _.metricName
  )

  setDefault(metricName, Rmse())

  override val params: Array[Parameter[_]] = Array(metricName, predictionColumn, labelColumn)

  override def getMetricName: String = $(metricName).name

}

object RegressionEvaluator {

  sealed abstract class Metric(override val name: String) extends Choice {

    override val params: Array[Parameter[_]] = Array()

    override val choiceOrder: List[Class[_ <: Choice]] = List(
      classOf[Mse],
      classOf[Rmse],
      classOf[R2],
      classOf[Mae]
    )

  }

  case class Mse() extends Metric("mse")

  case class Rmse() extends Metric("rmse")

  case class R2() extends Metric("r2")

  case class Mae() extends Metric("mae")

}
