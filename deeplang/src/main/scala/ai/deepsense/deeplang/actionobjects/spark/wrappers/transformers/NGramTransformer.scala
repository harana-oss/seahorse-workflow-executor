package ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers

import org.apache.spark.ml.feature.NGram

import ai.deepsense.deeplang.actionobjects.SparkTransformerAsMultiColumnTransformer
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper

class NGramTransformer extends SparkTransformerAsMultiColumnTransformer[NGram] {

  val n = new IntParameterWrapper[NGram](
    name = "n",
    description = Some("The minimum n-gram length."),
    sparkParamGetter = _.n,
    validator = RangeValidator(begin = 1.0, end = Int.MaxValue, step = Some(1.0))
  )

  setDefault(n, 2.0)

  override protected def getSpecificParams: Array[Parameter[_]] = Array(n)

  def setN(value: Int): this.type =
    set(n -> value)

}
