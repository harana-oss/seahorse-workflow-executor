package io.deepsense.deeplang.doperables.spark.wrappers.transformers

import org.apache.spark.ml.feature.PolynomialExpansion

import io.deepsense.deeplang.doperables.SparkTransformerAsMultiColumnTransformer
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.IntParamWrapper

class PolynomialExpander extends SparkTransformerAsMultiColumnTransformer[PolynomialExpansion] {

  override def convertInputNumericToVector: Boolean = true

  override def convertOutputVectorToDouble: Boolean = false

  val degree = new IntParamWrapper[PolynomialExpansion](
    name = "degree",
    description = Some("The polynomial degree to expand."),
    sparkParamGetter = _.degree,
    validator = RangeValidator(2.0, Int.MaxValue, step = Some(1.0))
  )

  setDefault(degree, 2.0)

  override protected def getSpecificParams: Array[Param[_]] = Array(degree)

}
