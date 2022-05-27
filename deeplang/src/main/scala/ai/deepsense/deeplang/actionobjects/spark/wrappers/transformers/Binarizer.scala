package ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers

import org.apache.spark.ml.feature.{Binarizer => SparkBinarizer}

import ai.deepsense.deeplang.actionobjects.SparkTransformerAsMultiColumnTransformer
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.wrappers.spark.DoubleParameterWrapper

class Binarizer extends SparkTransformerAsMultiColumnTransformer[SparkBinarizer] {

  val threshold = new DoubleParameterWrapper[SparkBinarizer](
    name = "threshold",
    description = Some("""The threshold used to binarize continuous features. Feature values greater
                         |than the threshold will be binarized to 1.0. Remaining values will be binarized
                         |to 0.0.""".stripMargin),
    sparkParamGetter = _.threshold
  )

  setDefault(threshold, 0.0)

  override protected def getSpecificParams: Array[Parameter[_]] = Array(threshold)

  def setThreshold(value: Double): this.type =
    set(threshold, value)

}
