package ai.deepsense.deeplang.doperables.spark.wrappers.transformers

import org.apache.spark.ml.feature.{OneHotEncoder => SparkOneHotEncoder}

import ai.deepsense.deeplang.doperables.SparkTransformerAsMultiColumnTransformer
import ai.deepsense.deeplang.params.Param
import ai.deepsense.deeplang.params.wrappers.spark.BooleanParamWrapper

class OneHotEncoder extends SparkTransformerAsMultiColumnTransformer[SparkOneHotEncoder] {

  val dropLast = new BooleanParamWrapper[SparkOneHotEncoder](
    name = "drop last",
    description = Some("Whether to drop the last category in the encoded vector."),
    sparkParamGetter = _.dropLast
  )

  setDefault(dropLast, true)

  override protected def getSpecificParams: Array[Param[_]] = Array(dropLast)

}
