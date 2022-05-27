package ai.deepsense.deeplang.actions.spark.wrappers.evaluators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.actionobjects.spark.wrappers.evaluators.RegressionEvaluator
import ai.deepsense.deeplang.actions.EvaluatorAsFactory

class CreateRegressionEvaluator extends EvaluatorAsFactory[RegressionEvaluator] with SparkOperationDocumentation {

  override val id: Id = "d9c3026c-a3d0-4365-8d1a-464a656b72de"

  override val name: String = "Regression Evaluator"

  override val description: String = "Creates a regression evaluator"

  override protected[this] val docsGuideLocation =
    Some("mllib-evaluation-metrics.html#regression-model-evaluation")

  override val since: Version = Version(1, 0, 0)

}
