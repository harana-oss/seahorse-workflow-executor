package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.ml.classification.{MultilayerPerceptronClassificationModel => SparkMultilayerPerceptronClassifierModel, MultilayerPerceptronClassifier => SparkMultilayerPerceptronClassifier}

import io.deepsense.deeplang.doperables.SparkEstimatorWrapper
import io.deepsense.deeplang.doperables.spark.wrappers.models.MultilayerPerceptronClassifierModel
import io.deepsense.deeplang.doperables.spark.wrappers.params.common._
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.validators.{ArrayLengthValidator, ComplexArrayValidator, RangeValidator}
import io.deepsense.deeplang.params.wrappers.spark.IntArrayParamWrapper

class MultilayerPerceptronClassifier
  extends SparkEstimatorWrapper[
    SparkMultilayerPerceptronClassifierModel,
    SparkMultilayerPerceptronClassifier,
    MultilayerPerceptronClassifierModel]
  with PredictorParams
  with HasLabelColumnParam
  with HasMaxIterationsParam
  with HasSeedParam
  with HasTolerance {

  override lazy val maxIterationsDefault = 100.0
  override lazy val toleranceDefault = 1E-4

  val layersParam = new IntArrayParamWrapper[SparkMultilayerPerceptronClassifier](
    name = "layers",
    description =
      Some("""The list of layer sizes that includes the input layer size as the first number and the
        |output layer size as the last number. The input layer and hidden layers have sigmoid
        |activation functions, while the output layer has a softmax. The input layer size has to be
        |equal to the length of the feature vector. The output layer size has to be equal to the
        |total number of labels.""".stripMargin),
    sparkParamGetter = _.layers,
    validator = ComplexArrayValidator(
      RangeValidator.positiveIntegers,
      ArrayLengthValidator.withAtLeast(2)
    )
  )
  setDefault(layersParam, Array(1.0, 1.0))

  override val params: Array[Param[_]] = Array(
    layersParam,
    maxIterations,
    seed,
    tolerance,
    labelColumn,
    featuresColumn,
    predictionColumn)
}
