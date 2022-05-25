package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.ml.feature.{IDF => SparkIDF, IDFModel => SparkIDFModel}

import io.deepsense.deeplang.doperables.SparkSingleColumnEstimatorWrapper
import io.deepsense.deeplang.doperables.spark.wrappers.models.IDFModel
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.IntParamWrapper


class IDFEstimator extends SparkSingleColumnEstimatorWrapper[SparkIDFModel, SparkIDF, IDFModel] {

  val minDocFreq = new IntParamWrapper[SparkIDF](
    name = "min documents frequency",
    description = Some("The minimum number of documents in which a term should appear."),
    sparkParamGetter = _.minDocFreq,
    validator = RangeValidator(begin = 0.0, end = Int.MaxValue, step = Some(1.0)))
  setDefault(minDocFreq, 0.0)

  override protected def getSpecificParams: Array[Param[_]] = Array(minDocFreq)
}
