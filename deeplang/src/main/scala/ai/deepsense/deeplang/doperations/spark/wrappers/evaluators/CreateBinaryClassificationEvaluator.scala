package ai.deepsense.deeplang.doperations.spark.wrappers.evaluators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.DOperation.Id
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.doperables.spark.wrappers.evaluators.BinaryClassificationEvaluator
import ai.deepsense.deeplang.doperations.EvaluatorAsFactory

class CreateBinaryClassificationEvaluator
    extends EvaluatorAsFactory[BinaryClassificationEvaluator]
    with OperationDocumentation {

  override val id: Id = "464ce3fa-e915-4a5d-a9d1-442c1e4b7aa7"

  override val name: String = "Binary Classification Evaluator"

  override val description: String = "Creates a binary classification evaluator"

  override val since: Version = Version(1, 0, 0)

}
