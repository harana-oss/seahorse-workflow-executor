package ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers

import org.apache.spark.ml.feature.DCT

import ai.deepsense.deeplang.actionobjects.SparkTransformerAsMultiColumnTransformer
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.wrappers.spark.BooleanParameterWrapper

class DiscreteCosineTransformer extends SparkTransformerAsMultiColumnTransformer[DCT] {

  override def convertInputNumericToVector: Boolean = true

  override def convertOutputVectorToDouble: Boolean = true

  val inverse = new BooleanParameterWrapper[DCT](
    name = "inverse",
    description = Some("Indicates whether to perform the inverse DCT (true) or forward DCT (false)."),
    sparkParamGetter = _.inverse
  )

  setDefault(inverse, false)

  override protected def getSpecificParams: Array[Parameter[_]] = Array(inverse)

}
