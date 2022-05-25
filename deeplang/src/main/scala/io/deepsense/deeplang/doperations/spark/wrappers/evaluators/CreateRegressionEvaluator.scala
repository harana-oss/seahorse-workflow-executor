package io.deepsense.deeplang.doperations.spark.wrappers.evaluators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.evaluators.RegressionEvaluator
import io.deepsense.deeplang.doperations.EvaluatorAsFactory

class CreateRegressionEvaluator
  extends EvaluatorAsFactory[RegressionEvaluator]
  with SparkOperationDocumentation {

  override val id: Id = "d9c3026c-a3d0-4365-8d1a-464a656b72de"
  override val name: String = "Regression Evaluator"
  override val description: String = "Creates a regression evaluator"

  override protected[this] val docsGuideLocation =
    Some("mllib-evaluation-metrics.html#regression-model-evaluation")
  override val since: Version = Version(1, 0, 0)
}
