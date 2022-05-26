package io.deepsense.deeplang.doperables.spark.wrappers.transformers

import org.apache.spark.ml.feature.{OneHotEncoder => SparkOneHotEncoder}

import io.deepsense.deeplang.doperables.SparkTransformerAsMultiColumnTransformer
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.wrappers.spark.BooleanParamWrapper

class OneHotEncoder extends SparkTransformerAsMultiColumnTransformer[SparkOneHotEncoder] {

  val dropLast = new BooleanParamWrapper[SparkOneHotEncoder](
    name = "drop last",
    description = Some("Whether to drop the last category in the encoded vector."),
    sparkParamGetter = _.dropLast
  )

  setDefault(dropLast, true)

  override protected def getSpecificParams: Array[Param[_]] = Array(dropLast)

}
