package io.deepsense.deeplang.doperables.spark.wrappers.params

import scala.language.reflectiveCalls

import org.apache.spark.ml

import io.deepsense.deeplang.doperables.spark.wrappers.params.common._
import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.IntParamWrapper

trait Word2VecParams extends Params
  with HasMaxIterationsParam
  with HasStepSizeParam
  with HasSeedParam {

  val vectorSize = new IntParamWrapper[ml.param.Params { val vectorSize: ml.param.IntParam }](
    name = "vector size",
    description = Some("The dimension of codes after transforming from words."),
    sparkParamGetter = _.vectorSize,
    validator = RangeValidator.positiveIntegers)
  setDefault(vectorSize -> 100)

  val numPartitions = new IntParamWrapper[ml.param.Params { val numPartitions: ml.param.IntParam }](
    name = "num partitions",
    description = Some("The number of partitions for sentences of words."),
    sparkParamGetter = _.numPartitions,
    validator = RangeValidator.positiveIntegers)
  setDefault(numPartitions -> 1)

  val minCount = new IntParamWrapper[ml.param.Params { val minCount: ml.param.IntParam }](
    name = "min count",
    description = Some("The minimum number of occurences of a token to " +
      "be included in the model's vocabulary."),
    sparkParamGetter = _.minCount,
    validator = RangeValidator.positiveIntegers)
  setDefault(minCount -> 5)

  def setMinCount(value: Int): this.type = {
    set(minCount -> value)
  }

  def setVectorSize(value: Int): this.type = {
    set(vectorSize -> value)
  }
}
