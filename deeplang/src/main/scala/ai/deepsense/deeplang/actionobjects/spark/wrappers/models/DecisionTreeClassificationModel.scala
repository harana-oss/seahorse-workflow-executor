package ai.deepsense.deeplang.actionobjects.spark.wrappers.models

import org.apache.spark.ml.classification.{DecisionTreeClassificationModel => SparkDecisionTreeClassificationModel}
import org.apache.spark.ml.classification.{DecisionTreeClassifier => SparkDecisionTreeClassifier}

import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.ProbabilisticClassifierParams
import ai.deepsense.deeplang.actionobjects.stringindexingwrapper.StringIndexingWrapperModel
import ai.deepsense.deeplang.actionobjects.LoadableWithFallback
import ai.deepsense.deeplang.actionobjects.SparkModelWrapper
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.sparkutils.ML

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

  override val params: Array[Parameter[_]] = Array(featuresColumn, probabilityColumn, rawPredictionColumn, predictionColumn)

  override protected def transformerName: String =
    classOf[DecisionTreeClassificationModel].getSimpleName

  override def tryToLoadModel(path: String): Option[SparkDecisionTreeClassificationModel] =
    ML.ModelLoading.decisionTreeClassification(path)

}
