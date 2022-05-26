package ai.deepsense.deeplang.doperations.spark.wrappers.estimators

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.DOperation.Id
import ai.deepsense.deeplang.documentation.SparkOperationDocumentation
import ai.deepsense.deeplang.doperables.spark.wrappers.estimators.MultilayerPerceptronClassifier
import ai.deepsense.deeplang.doperations.EstimatorAsFactory

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
