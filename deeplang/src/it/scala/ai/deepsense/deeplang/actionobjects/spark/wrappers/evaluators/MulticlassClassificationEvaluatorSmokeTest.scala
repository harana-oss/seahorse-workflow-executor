package ai.deepsense.deeplang.actionobjects.spark.wrappers.evaluators

import ai.deepsense.deeplang.actionobjects.AbstractEvaluatorSmokeTest
import ai.deepsense.deeplang.parameters.ParamPair
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

class MulticlassClassificationEvaluatorSmokeTest extends AbstractEvaluatorSmokeTest {

  override def className: String = "MulticlassClassificationEvaluator"

  override val evaluator: MulticlassClassificationEvaluator =
    new MulticlassClassificationEvaluator()

  override val evaluatorParams: Seq[ParamPair[_]] = Seq(
    evaluator.metricName       -> MulticlassClassificationEvaluator.F1(),
    evaluator.predictionColumn -> NameSingleColumnSelection("prediction"),
    evaluator.labelColumn      -> NameSingleColumnSelection("label")
  )

}
