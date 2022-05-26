package io.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.classification.{DecisionTreeClassificationModel => SparkDecisionTreeClassificationModel}
import org.apache.spark.ml.classification.{DecisionTreeClassifier => SparkDecisionTreeClassifier}

import io.deepsense.deeplang.doperables.spark.wrappers.params.common.ProbabilisticClassifierParams
import io.deepsense.deeplang.doperables.stringindexingwrapper.StringIndexingWrapperModel
import io.deepsense.deeplang.doperables.LoadableWithFallback
import io.deepsense.deeplang.doperables.SparkModelWrapper
import io.deepsense.deeplang.params.Param
import io.deepsense.sparkutils.ML

class DecisionTreeClassificationModel(vanillaModel: VanillaDecisionTreeClassificationModel)
    extends StringIndexingWrapperModel[SparkDecisionTreeClassificationModel, SparkDecisionTreeClassifier](
      vanillaModel
    ) {

  def this() = this(new VanillaDecisionTreeClassificationModel())

}

class VanillaDecisionTreeClassificationModel
    extends SparkModelWrapper[SparkDecisionTreeClassificationModel, SparkDecisionTreeClassifier]
    with ProbabilisticClassifierParams
    with LoadableWithFallback[SparkDecisionTreeClassificationModel, SparkDecisionTreeClassifier] {

  override val params: Array[Param[_]] = Array(featuresColumn, probabilityColumn, rawPredictionColumn, predictionColumn)

  override protected def transformerName: String =
    classOf[DecisionTreeClassificationModel].getSimpleName

  override def tryToLoadModel(path: String): Option[SparkDecisionTreeClassificationModel] =
    ML.ModelLoading.decisionTreeClassification(path)

}
