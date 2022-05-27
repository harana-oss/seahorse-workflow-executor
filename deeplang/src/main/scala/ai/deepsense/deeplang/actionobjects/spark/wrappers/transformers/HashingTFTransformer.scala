package ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers

import org.apache.spark.ml.feature.HashingTF

import ai.deepsense.deeplang.actionobjects.SparkTransformerAsMultiColumnTransformer
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper

class HashingTFTransformer extends SparkTransformerAsMultiColumnTransformer[HashingTF] {

  val numFeatures = new IntParameterWrapper[HashingTF](
    name = "num features",
    description = Some("The number of features."),
    sparkParamGetter = _.numFeatures,
    validator = RangeValidator(1.0, Int.MaxValue, step = Some(1.0))
  )

  // With default setting in Bundled Image (1 << 20) makes jvm run out of memory even for few rows.
  setDefault(numFeatures, (1 << 18).toDouble)

  override protected def getSpecificParams: Array[Parameter[_]] = Array(numFeatures)

  def setNumFeatures(value: Int): this.type =
    set(numFeatures -> value)

}
