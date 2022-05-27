package ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers

import org.apache.spark.ml.feature.{Normalizer => SparkNormalizer}

import ai.deepsense.deeplang.actionobjects.SparkTransformerAsMultiColumnTransformer
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.DoubleParameterWrapper

class Normalizer extends SparkTransformerAsMultiColumnTransformer[SparkNormalizer] {

  override def convertInputNumericToVector: Boolean = true

  override def convertOutputVectorToDouble: Boolean = true

  val p = new DoubleParameterWrapper[SparkNormalizer](
    name = "p",
    description = Some("Normalization in L^p space."),
    sparkParamGetter = _.p,
    validator = RangeValidator(1.0, Double.PositiveInfinity)
  )

  setDefault(p, 2.0)

  override protected def getSpecificParams: Array[Parameter[_]] = Array(p)

}
