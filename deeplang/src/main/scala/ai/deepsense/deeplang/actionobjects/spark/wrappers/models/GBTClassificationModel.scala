package ai.deepsense.deeplang.actionobjects.spark.wrappers.models

import org.apache.spark.ml.classification.{GBTClassificationModel => SparkGBTClassificationModel}
import org.apache.spark.ml.classification.{GBTClassifier => SparkGBTClassifier}
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

import ai.deepsense.commons.utils.Logging
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.PredictorParams
import ai.deepsense.deeplang.actionobjects.stringindexingwrapper.StringIndexingWrapperModel
import ai.deepsense.deeplang.actionobjects.LoadableWithFallback
import ai.deepsense.deeplang.actionobjects.SparkModelWrapper
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.sparkutils.ML

class GBTClassificationModel(vanilaModel: VanillaGBTClassificationModel)
    extends StringIndexingWrapperModel[SparkGBTClassificationModel, SparkGBTClassifier](vanilaModel) {

  def this() = this(new VanillaGBTClassificationModel())

}

class VanillaGBTClassificationModel()
    extends SparkModelWrapper[SparkGBTClassificationModel, SparkGBTClassifier]
    with LoadableWithFallback[SparkGBTClassificationModel, SparkGBTClassifier]
    with PredictorParams
    with Logging {

  override protected def applyTransformSchema(schema: StructType): Option[StructType] = {
    val predictionColumnName = $(predictionColumn)
    Some(StructType(schema.fields :+ StructField(predictionColumnName, DoubleType)))
  }

  override val params: Array[Parameter[_]] =
    Array(featuresColumn, predictionColumn)

  override def report(extended: Boolean = true): Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "number of features",
          value = sparkModel.numFeatures,
          description = "Number of features the model was trained on."
        )
      )

    super
      .report(extended)
      .withReportName(s"${this.getClass.getSimpleName} with ${sparkModel.numTrees} trees")
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
      .withAdditionalTable(CommonTablesGenerators.decisionTree(sparkModel.treeWeights, sparkModel.trees), 2)
  }

  override protected def transformerName: String = classOf[GBTClassificationModel].getSimpleName

  override def tryToLoadModel(path: String): Option[SparkGBTClassificationModel] =
    ML.ModelLoading.GBTClassification(path)

}
