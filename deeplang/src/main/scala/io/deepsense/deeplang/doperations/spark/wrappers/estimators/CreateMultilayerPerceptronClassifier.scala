package io.deepsense.deeplang.doperations.spark.wrappers.estimators

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.SparkOperationDocumentation
import io.deepsense.deeplang.doperables.spark.wrappers.estimators.MultilayerPerceptronClassifier
import io.deepsense.deeplang.doperations.EstimatorAsFactory

class CreateMultilayerPerceptronClassifier
  extends EstimatorAsFactory[MultilayerPerceptronClassifier]
  with SparkOperationDocumentation {

  override val id: Id = "860f51aa-627e-4636-a4df-696b79a54efc"
  override val name: String = "Multilayer Perceptron Classifier"
  override val description: String = "Creates a Multilayer Perceptron classification model."

  override protected[this] val docsGuideLocation =
    Some("ml-classification-regression.html#multilayer-perceptron-classifier")
  override val since: Version = Version(1, 1, 0)
}
