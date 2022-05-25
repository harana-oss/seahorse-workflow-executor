package io.deepsense.deeplang.doperables.spark.wrappers.evaluators

import io.deepsense.deeplang.doperables.AbstractEvaluatorSmokeTest
import io.deepsense.deeplang.params.ParamPair
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class MulticlassClassificationEvaluatorSmokeTest extends AbstractEvaluatorSmokeTest {

  override def className: String = "MulticlassClassificationEvaluator"

  override val evaluator: MulticlassClassificationEvaluator =
    new MulticlassClassificationEvaluator()

  override val evaluatorParams: Seq[ParamPair[_]] = Seq(
    evaluator.metricName -> MulticlassClassificationEvaluator.F1(),
    evaluator.predictionColumn -> NameSingleColumnSelection("prediction"),
    evaluator.labelColumn -> NameSingleColumnSelection("label")
  )
}
