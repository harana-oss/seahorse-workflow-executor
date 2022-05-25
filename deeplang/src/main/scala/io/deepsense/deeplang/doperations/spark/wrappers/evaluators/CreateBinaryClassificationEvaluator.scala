package io.deepsense.deeplang.doperations.spark.wrappers.evaluators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.evaluators.BinaryClassificationEvaluator
import io.deepsense.deeplang.doperations.EvaluatorAsFactory

class CreateBinaryClassificationEvaluator
  extends EvaluatorAsFactory[BinaryClassificationEvaluator] with OperationDocumentation {

  override val id: Id = "464ce3fa-e915-4a5d-a9d1-442c1e4b7aa7"
  override val name: String = "Binary Classification Evaluator"
  override val description: String = "Creates a binary classification evaluator"

  override val since: Version = Version(1, 0, 0)
}
