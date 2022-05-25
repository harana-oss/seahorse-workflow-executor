package io.deepsense.deeplang.doperables.spark.wrappers.evaluators

import io.deepsense.deeplang.doperables.AbstractEvaluatorSmokeTest
import io.deepsense.deeplang.params.ParamPair
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class RegressionEvaluatorSmokeTest extends AbstractEvaluatorSmokeTest {

  override def className: String = "RegressionEvaluator"

  override val evaluator: RegressionEvaluator = new RegressionEvaluator()

  override val evaluatorParams: Seq[ParamPair[_]] = Seq(
    evaluator.metricName -> RegressionEvaluator.Rmse(),
    evaluator.predictionColumn -> NameSingleColumnSelection("prediction"),
    evaluator.labelColumn -> NameSingleColumnSelection("label")
  )
}
