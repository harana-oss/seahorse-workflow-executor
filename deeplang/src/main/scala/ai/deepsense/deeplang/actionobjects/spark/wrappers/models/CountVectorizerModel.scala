package ai.deepsense.deeplang.actionobjects.spark.wrappers.models

import org.apache.spark.ml.feature.{CountVectorizer => SparkCountVectorizer}
import org.apache.spark.ml.feature.{CountVectorizerModel => SparkCountVectorizerModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.SparkSingleColumnModelWrapper
import ai.deepsense.deeplang.actionobjects.serialization.SerializableSparkModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasMinTermsFrequencyParam
import ai.deepsense.deeplang.parameters.Parameter

class CountVectorizerModel
    extends SparkSingleColumnModelWrapper[SparkCountVectorizerModel, SparkCountVectorizer]
    with HasMinTermsFrequencyParam {

  override protected def getSpecificParams: Array[Parameter[_]] = Array(minTF)

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String
  ): SerializableSparkModel[SparkCountVectorizerModel] =
    new SerializableSparkModel(SparkCountVectorizerModel.load(path))

}
