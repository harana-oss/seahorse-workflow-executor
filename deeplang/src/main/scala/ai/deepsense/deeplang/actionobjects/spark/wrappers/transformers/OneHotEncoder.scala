package ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers

import org.apache.spark.ml.feature.{OneHotEncoder => SparkOneHotEncoder}

import ai.deepsense.deeplang.actionobjects.SparkTransformerAsMultiColumnTransformer
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.wrappers.spark.BooleanParameterWrapper

class OneHotEncoder extends SparkTransformerAsMultiColumnTransformer[SparkOneHotEncoder] {

  val dropLast = new BooleanParameterWrapper[SparkOneHotEncoder](
    name = "drop last",
    description = Some("Whether to drop the last category in the encoded vector."),
    sparkParamGetter = _.dropLast
  )

  setDefault(dropLast, true)

  override protected def getSpecificParams: Array[Parameter[_]] = Array(dropLast)

}
