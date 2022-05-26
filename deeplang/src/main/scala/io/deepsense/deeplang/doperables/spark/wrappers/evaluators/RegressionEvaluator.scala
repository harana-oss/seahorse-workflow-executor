package io.deepsense.deeplang.doperables.spark.wrappers.evaluators

import org.apache.spark.ml.evaluation.{RegressionEvaluator => SparkRegressionEvaluator}

import io.deepsense.deeplang.doperables.SparkEvaluatorWrapper
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.HasLabelColumnParam
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.HasPredictionColumnSelectorParam
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.choice.Choice
import io.deepsense.deeplang.params.wrappers.spark.ChoiceParamWrapper

class RegressionEvaluator
    extends SparkEvaluatorWrapper[SparkRegressionEvaluator]
    with HasPredictionColumnSelectorParam
    with HasLabelColumnParam {

  import RegressionEvaluator._

  val metricName = new ChoiceParamWrapper[SparkRegressionEvaluator, Metric](
    name = "regression metric",
    description = Some("The metric used in evaluation."),
    sparkParamGetter = _.metricName
  )

  setDefault(metricName, Rmse())

  override val params: Array[Param[_]] = Array(metricName, predictionColumn, labelColumn)

  override def getMetricName: String = $(metricName).name

}

object RegressionEvaluator {

  sealed abstract class Metric(override val name: String) extends Choice {

    override val params: Array[Param[_]] = Array()

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
