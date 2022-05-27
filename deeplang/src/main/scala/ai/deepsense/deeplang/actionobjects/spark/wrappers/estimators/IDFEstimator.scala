package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import org.apache.spark.ml.feature.{IDF => SparkIDF}
import org.apache.spark.ml.feature.{IDFModel => SparkIDFModel}

import ai.deepsense.deeplang.actionobjects.SparkSingleColumnEstimatorWrapper
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.IDFModel
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper

class IDFEstimator extends SparkSingleColumnEstimatorWrapper[SparkIDFModel, SparkIDF, IDFModel] {

  val minDocFreq = new IntParameterWrapper[SparkIDF](
    name = "min documents frequency",
    description = Some("The minimum number of documents in which a term should appear."),
    sparkParamGetter = _.minDocFreq,
    validator = RangeValidator(begin = 0.0, end = Int.MaxValue, step = Some(1.0))
  )

  setDefault(minDocFreq, 0.0)

  override protected def getSpecificParams: Array[Parameter[_]] = Array(minDocFreq)

}
