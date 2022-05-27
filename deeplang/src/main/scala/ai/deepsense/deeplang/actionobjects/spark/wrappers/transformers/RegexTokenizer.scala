package ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers

import org.apache.spark.ml.feature.{RegexTokenizer => SparkRegexTokenizer}

import ai.deepsense.deeplang.actionobjects.SparkTransformerAsMultiColumnTransformer
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.BooleanParameterWrapper
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper
import ai.deepsense.deeplang.parameters.wrappers.spark.StringParameterWrapper

class RegexTokenizer extends SparkTransformerAsMultiColumnTransformer[SparkRegexTokenizer] {

  val gaps = new BooleanParameterWrapper[SparkRegexTokenizer](
    name = "gaps",
    description = Some("Indicates whether the regex splits on gaps (true) or matches tokens (false)."),
    sparkParamGetter = _.gaps
  )

  setDefault(gaps, true)

  val minTokenLength = new IntParameterWrapper[SparkRegexTokenizer](
    name = "min token length",
    description = Some("The minimum token length."),
    sparkParamGetter = _.minTokenLength,
    validator = RangeValidator.positiveIntegers
  )

  setDefault(minTokenLength, 1.0)

  val pattern = new StringParameterWrapper[SparkRegexTokenizer](
    name = "pattern",
    description = Some("""The regex pattern used to match delimiters (gaps = true) or tokens
                         |(gaps = false).""".stripMargin),
    sparkParamGetter = _.pattern
  )

  setDefault(pattern, "\\s+")

  override protected def getSpecificParams: Array[Parameter[_]] = Array(gaps, minTokenLength, pattern)

}
