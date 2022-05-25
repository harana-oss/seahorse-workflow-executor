package io.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.feature.{CountVectorizer => SparkCountVectorizer, CountVectorizerModel => SparkCountVectorizerModel}

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.SparkSingleColumnModelWrapper
import io.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.HasMinTermsFrequencyParam
import io.deepsense.deeplang.params.Param

class CountVectorizerModel
  extends SparkSingleColumnModelWrapper[SparkCountVectorizerModel, SparkCountVectorizer]
  with HasMinTermsFrequencyParam {

  override protected def getSpecificParams: Array[Param[_]] = Array(minTF)

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String): SerializableSparkModel[SparkCountVectorizerModel] = {
    new SerializableSparkModel(SparkCountVectorizerModel.load(path))
  }
}
