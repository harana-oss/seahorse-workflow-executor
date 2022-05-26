package ai.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.ml.feature.{PCA => SparkPCA}
import org.apache.spark.ml.feature.{PCAModel => SparkPCAModel}

import ai.deepsense.deeplang.doperables.SparkSingleColumnEstimatorWrapper
import ai.deepsense.deeplang.doperables.spark.wrappers.models.PCAModel
import ai.deepsense.deeplang.params.Param
import ai.deepsense.deeplang.params.validators.RangeValidator
import ai.deepsense.deeplang.params.wrappers.spark._

class PCAEstimator extends SparkSingleColumnEstimatorWrapper[SparkPCAModel, SparkPCA, PCAModel] {

  val k = new IntParamWrapper[SparkPCA](
    name = "k",
    description = Some("The number of principal components."),
    sparkParamGetter = _.k,
    validator = RangeValidator(begin = 1.0, end = Int.MaxValue, step = Some(1.0))
  )

  setDefault(k, 1.0)

  override protected def getSpecificParams: Array[Param[_]] = Array(k)

  def setK(value: Int): this.type =
    set(k -> value)

}
