package io.deepsense.deeplang.doperables.spark.wrappers.transformers

import org.apache.spark.ml.feature.NGram

import io.deepsense.deeplang.doperables.SparkTransformerAsMultiColumnTransformer
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.IntParamWrapper

class NGramTransformer extends SparkTransformerAsMultiColumnTransformer[NGram] {

  val n = new IntParamWrapper[NGram](
    name = "n",
    description = Some("The minimum n-gram length."),
    sparkParamGetter = _.n,
    validator = RangeValidator(begin = 1.0, end = Int.MaxValue, step = Some(1.0)))
  setDefault(n, 2.0)

  override protected def getSpecificParams: Array[Param[_]] = Array(n)

  def setN(value: Int): this.type = {
    set(n -> value)
  }
}
