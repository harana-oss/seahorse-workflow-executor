package ai.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.feature.{CountVectorizer => SparkCountVectorizer}
import org.apache.spark.ml.feature.{CountVectorizerModel => SparkCountVectorizerModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.doperables.SparkSingleColumnModelWrapper
import ai.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasMinTermsFrequencyParam
import ai.deepsense.deeplang.params.Param

class CountVectorizerModel
    extends SparkSingleColumnModelWrapper[SparkCountVectorizerModel, SparkCountVectorizer]
    with HasMinTermsFrequencyParam {

  override protected def getSpecificParams: Array[Param[_]] = Array(minTF)

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String
  ): SerializableSparkModel[SparkCountVectorizerModel] =
    new SerializableSparkModel(SparkCountVectorizerModel.load(path))

}
