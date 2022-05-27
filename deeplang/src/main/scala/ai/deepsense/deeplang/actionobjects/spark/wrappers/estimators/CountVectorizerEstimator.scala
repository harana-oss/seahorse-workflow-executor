package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.feature.{CountVectorizer => SparkCountVectorizer}
import org.apache.spark.ml.feature.{CountVectorizerModel => SparkCountVectorizerModel}

import ai.deepsense.deeplang.actionobjects.SparkSingleColumnEstimatorWrapper
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.CountVectorizerModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common._
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.DoubleParameterWrapper
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper

class CountVectorizerEstimator
    extends SparkSingleColumnEstimatorWrapper[SparkCountVectorizerModel, SparkCountVectorizer, CountVectorizerModel]
    with HasMinTermsFrequencyParam {

  val minDF = new DoubleParameterWrapper[ml.param.Params { val minDF: ml.param.DoubleParam }](
    name = "min different documents",
    description = Some(
      "Specifies the minimum number of different documents " +
        "a term must appear in to be included in the vocabulary."
    ),
    sparkParamGetter = _.minDF,
    RangeValidator(0.0, Double.MaxValue)
  )

  setDefault(minDF, 1.0)

  val vocabSize = new IntParameterWrapper[ml.param.Params { val vocabSize: ml.param.IntParam }](
    name = "max vocabulary size",
    description = Some("The maximum size of the vocabulary."),
    sparkParamGetter = _.vocabSize,
    RangeValidator(0.0, Int.MaxValue, beginIncluded = false, step = Some(1.0))
  )

  setDefault(vocabSize, (1 << 18).toDouble)

  override protected def getSpecificParams: Array[Parameter[_]] = Array(vocabSize, minDF, minTF)

}
