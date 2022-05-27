package ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers

import org.apache.spark.ml.feature.PolynomialExpansion

import ai.deepsense.deeplang.actionobjects.SparkTransformerAsMultiColumnTransformer
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper

class PolynomialExpander extends SparkTransformerAsMultiColumnTransformer[PolynomialExpansion] {

  override def convertInputNumericToVector: Boolean = true

  override def convertOutputVectorToDouble: Boolean = false

  val degree = new IntParameterWrapper[PolynomialExpansion](
    name = "degree",
    description = Some("The polynomial degree to expand."),
    sparkParamGetter = _.degree,
    validator = RangeValidator(2.0, Int.MaxValue, step = Some(1.0))
  )

  setDefault(degree, 2.0)

  override protected def getSpecificParams: Array[Parameter[_]] = Array(degree)

}
