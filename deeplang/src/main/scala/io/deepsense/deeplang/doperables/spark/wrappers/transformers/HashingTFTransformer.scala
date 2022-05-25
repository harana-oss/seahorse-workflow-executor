package io.deepsense.deeplang.doperables.spark.wrappers.transformers

import org.apache.spark.ml.feature.HashingTF

import io.deepsense.deeplang.doperables.SparkTransformerAsMultiColumnTransformer
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.IntParamWrapper

class HashingTFTransformer extends SparkTransformerAsMultiColumnTransformer[HashingTF] {

  val numFeatures = new IntParamWrapper[HashingTF](
    name = "num features",
    description = Some("The number of features."),
    sparkParamGetter = _.numFeatures,
    validator = RangeValidator(1.0, Int.MaxValue, step = Some(1.0)))
  // With default setting in Bundled Image (1 << 20) makes jvm run out of memory even for few rows.
  setDefault(numFeatures, (1 << 18).toDouble)

  override protected def getSpecificParams: Array[Param[_]] = Array(numFeatures)

  def setNumFeatures(value: Int): this.type = {
    set(numFeatures -> value)
  }
}
