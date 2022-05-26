package ai.deepsense.deeplang.doperations.spark.wrappers.evaluators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.DOperation.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.doperables.spark.wrappers.evaluators.MulticlassClassificationEvaluator
import ai.deepsense.deeplang.doperations.EvaluatorAsFactory

class CreateMulticlassClassificationEvaluator
    extends EvaluatorAsFactory[MulticlassClassificationEvaluator]
    with SparkOperationDocumentation {

  override val id: Id = "3129848c-8a1c-449e-b006-340fec5b42ae"

  override val name: String = "Multiclass Classification Evaluator"

  override val description: String = "Creates a multiclass classification evaluator. " +
    "Multiclass classification evaluator does not assume any label class is special, " +
    "thus it cannot be used for calculation of metrics specific for binary classification " +
    "(where this assumption is taken into account)."

  override protected[this] val docsGuideLocation =
    Some("mllib-evaluation-metrics.html#multiclass-classification")

  override val since: Version = Version(1, 0, 0)

}
