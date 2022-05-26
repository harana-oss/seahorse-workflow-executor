package ai.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.ml.feature.{IDF => SparkIDF}
import org.apache.spark.ml.feature.{IDFModel => SparkIDFModel}

import ai.deepsense.deeplang.doperables.SparkSingleColumnEstimatorWrapper
import ai.deepsense.deeplang.doperables.spark.wrappers.models.IDFModel
import ai.deepsense.deeplang.params.Param
import ai.deepsense.deeplang.params.validators.RangeValidator
import ai.deepsense.deeplang.params.wrappers.spark.IntParamWrapper

class IDFEstimator extends SparkSingleColumnEstimatorWrapper[SparkIDFModel, SparkIDF, IDFModel] {

  val minDocFreq = new IntParamWrapper[SparkIDF](
    name = "min documents frequency",
    description = Some("The minimum number of documents in which a term should appear."),
    sparkParamGetter = _.minDocFreq,
    validator = RangeValidator(begin = 0.0, end = Int.MaxValue, step = Some(1.0))
  )

  setDefault(minDocFreq, 0.0)

  override protected def getSpecificParams: Array[Param[_]] = Array(minDocFreq)

}
