package ai.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.ml.feature.{VectorIndexer => SparkVectorIndexer}
import org.apache.spark.ml.feature.{VectorIndexerModel => SparkVectorIndexerModel}

import ai.deepsense.deeplang.doperables.SparkSingleColumnEstimatorWrapper
import ai.deepsense.deeplang.doperables.spark.wrappers.models.VectorIndexerModel
import ai.deepsense.deeplang.params.Param
import ai.deepsense.deeplang.params.validators.RangeValidator
import ai.deepsense.deeplang.params.wrappers.spark.IntParamWrapper

class VectorIndexerEstimator
    extends SparkSingleColumnEstimatorWrapper[SparkVectorIndexerModel, SparkVectorIndexer, VectorIndexerModel] {

  val maxCategories = new IntParamWrapper[SparkVectorIndexer](
    name = "max categories",
    description = Some("""The threshold for the number of values a categorical feature can take.
                         |If a feature is found to have more values, then it is declared continuous.""".stripMargin),
    sparkParamGetter = _.maxCategories,
    validator = RangeValidator(begin = 2.0, end = Int.MaxValue, step = Some(1.0))
  )

  setDefault(maxCategories, 20.0)

  override protected def getSpecificParams: Array[Param[_]] = Array(maxCategories)

  def setMaxCategories(value: Int): this.type =
    set(maxCategories -> value)

}
