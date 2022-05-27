package ai.deepsense.deeplang.actionobjects.spark.wrappers.evaluators

import ai.deepsense.deeplang.actionobjects.AbstractEvaluatorSmokeTest
import ai.deepsense.deeplang.parameters.ParamPair
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

class RegressionEvaluatorSmokeTest extends AbstractEvaluatorSmokeTest {

  override def className: String = "RegressionEvaluator"

  override val evaluator: RegressionEvaluator = new RegressionEvaluator()

  override val evaluatorParams: Seq[ParamPair[_]] = Seq(
    evaluator.metricName       -> RegressionEvaluator.Rmse(),
    evaluator.predictionColumn -> NameSingleColumnSelection("prediction"),
    evaluator.labelColumn      -> NameSingleColumnSelection("label")
  )

}
