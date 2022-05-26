package io.deepsense.deeplang.doperables.spark.wrappers.transformers

import org.apache.spark.ml.feature.{Normalizer => SparkNormalizer}

import io.deepsense.deeplang.doperables.SparkTransformerAsMultiColumnTransformer
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.DoubleParamWrapper

class Normalizer extends SparkTransformerAsMultiColumnTransformer[SparkNormalizer] {

  override def convertInputNumericToVector: Boolean = true

  override def convertOutputVectorToDouble: Boolean = true

  val p = new DoubleParamWrapper[SparkNormalizer](
    name = "p",
    description = Some("Normalization in L^p space."),
    sparkParamGetter = _.p,
    validator = RangeValidator(1.0, Double.PositiveInfinity)
  )

  setDefault(p, 2.0)

  override protected def getSpecificParams: Array[Param[_]] = Array(p)

}
