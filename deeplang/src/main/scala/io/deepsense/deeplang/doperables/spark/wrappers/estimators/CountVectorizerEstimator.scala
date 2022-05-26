package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.feature.{CountVectorizer => SparkCountVectorizer}
import org.apache.spark.ml.feature.{CountVectorizerModel => SparkCountVectorizerModel}

import io.deepsense.deeplang.doperables.SparkSingleColumnEstimatorWrapper
import io.deepsense.deeplang.doperables.spark.wrappers.models.CountVectorizerModel
import io.deepsense.deeplang.doperables.spark.wrappers.params.common._
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.DoubleParamWrapper
import io.deepsense.deeplang.params.wrappers.spark.IntParamWrapper

class CountVectorizerEstimator
    extends SparkSingleColumnEstimatorWrapper[SparkCountVectorizerModel, SparkCountVectorizer, CountVectorizerModel]
    with HasMinTermsFrequencyParam {

  val minDF = new DoubleParamWrapper[ml.param.Params { val minDF: ml.param.DoubleParam }](
    name = "min different documents",
    description = Some(
      "Specifies the minimum number of different documents " +
        "a term must appear in to be included in the vocabulary."
    ),
    sparkParamGetter = _.minDF,
    RangeValidator(0.0, Double.MaxValue)
  )

  setDefault(minDF, 1.0)

  val vocabSize = new IntParamWrapper[ml.param.Params { val vocabSize: ml.param.IntParam }](
    name = "max vocabulary size",
    description = Some("The maximum size of the vocabulary."),
    sparkParamGetter = _.vocabSize,
    RangeValidator(0.0, Int.MaxValue, beginIncluded = false, step = Some(1.0))
  )

  setDefault(vocabSize, (1 << 18).toDouble)

  override protected def getSpecificParams: Array[Param[_]] = Array(vocabSize, minDF, minTF)

}
