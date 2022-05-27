package ai.deepsense.deeplang.actionobjects.spark.wrappers.params

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common._
import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper

trait Word2VecParams extends Params with HasMaxIterationsParam with HasStepSizeParam with HasSeedParam {

  val vectorSize = new IntParameterWrapper[ml.param.Params { val vectorSize: ml.param.IntParam }](
    name = "vector size",
    description = Some("The dimension of codes after transforming from words."),
    sparkParamGetter = _.vectorSize,
    validator = RangeValidator.positiveIntegers
  )

  setDefault(vectorSize -> 100)

  val numPartitions = new IntParameterWrapper[ml.param.Params { val numPartitions: ml.param.IntParam }](
    name = "num partitions",
    description = Some("The number of partitions for sentences of words."),
    sparkParamGetter = _.numPartitions,
    validator = RangeValidator.positiveIntegers
  )

  setDefault(numPartitions -> 1)

  val minCount = new IntParameterWrapper[ml.param.Params { val minCount: ml.param.IntParam }](
    name = "min count",
    description = Some(
      "The minimum number of occurences of a token to " +
        "be included in the model's vocabulary."
    ),
    sparkParamGetter = _.minCount,
    validator = RangeValidator.positiveIntegers
  )

  setDefault(minCount -> 5)

  def setMinCount(value: Int): this.type =
    set(minCount -> value)

  def setVectorSize(value: Int): this.type =
    set(vectorSize -> value)

}
