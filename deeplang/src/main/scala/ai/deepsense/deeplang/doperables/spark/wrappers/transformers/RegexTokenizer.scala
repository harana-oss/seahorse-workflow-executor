package ai.deepsense.deeplang.doperables.spark.wrappers.transformers

import org.apache.spark.ml.feature.{RegexTokenizer => SparkRegexTokenizer}

import ai.deepsense.deeplang.doperables.SparkTransformerAsMultiColumnTransformer
import ai.deepsense.deeplang.params.Param
import ai.deepsense.deeplang.params.validators.RangeValidator
import ai.deepsense.deeplang.params.wrappers.spark.BooleanParamWrapper
import ai.deepsense.deeplang.params.wrappers.spark.IntParamWrapper
import ai.deepsense.deeplang.params.wrappers.spark.StringParamWrapper

class RegexTokenizer extends SparkTransformerAsMultiColumnTransformer[SparkRegexTokenizer] {

  val gaps = new BooleanParamWrapper[SparkRegexTokenizer](
    name = "gaps",
    description = Some("Indicates whether the regex splits on gaps (true) or matches tokens (false)."),
    sparkParamGetter = _.gaps
  )

  setDefault(gaps, true)

  val minTokenLength = new IntParamWrapper[SparkRegexTokenizer](
    name = "min token length",
    description = Some("The minimum token length."),
    sparkParamGetter = _.minTokenLength,
    validator = RangeValidator.positiveIntegers
  )

  setDefault(minTokenLength, 1.0)

  val pattern = new StringParamWrapper[SparkRegexTokenizer](
    name = "pattern",
    description = Some("""The regex pattern used to match delimiters (gaps = true) or tokens
                         |(gaps = false).""".stripMargin),
    sparkParamGetter = _.pattern
  )

  setDefault(pattern, "\\s+")

  override protected def getSpecificParams: Array[Param[_]] = Array(gaps, minTokenLength, pattern)

}
