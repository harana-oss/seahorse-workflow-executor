package ai.deepsense.deeplang.doperables.spark.wrappers.transformers

import org.apache.spark.ml.feature.DCT

import ai.deepsense.deeplang.doperables.SparkTransformerAsMultiColumnTransformer
import ai.deepsense.deeplang.params.Param
import ai.deepsense.deeplang.params.wrappers.spark.BooleanParamWrapper

class DiscreteCosineTransformer extends SparkTransformerAsMultiColumnTransformer[DCT] {

  override def convertInputNumericToVector: Boolean = true

  override def convertOutputVectorToDouble: Boolean = true

  val inverse = new BooleanParamWrapper[DCT](
    name = "inverse",
    description = Some("Indicates whether to perform the inverse DCT (true) or forward DCT (false)."),
    sparkParamGetter = _.inverse
  )

  setDefault(inverse, false)

  override protected def getSpecificParams: Array[Param[_]] = Array(inverse)

}
