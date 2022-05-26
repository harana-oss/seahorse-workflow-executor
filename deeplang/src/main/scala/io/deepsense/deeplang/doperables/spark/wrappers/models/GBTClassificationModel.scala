package io.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.classification.{GBTClassificationModel => SparkGBTClassificationModel}
import org.apache.spark.ml.classification.{GBTClassifier => SparkGBTClassifier}
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

import io.deepsense.commons.utils.Logging
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.PredictorParams
import io.deepsense.deeplang.doperables.stringindexingwrapper.StringIndexingWrapperModel
import io.deepsense.deeplang.doperables.LoadableWithFallback
import io.deepsense.deeplang.doperables.SparkModelWrapper
import io.deepsense.deeplang.params.Param
import io.deepsense.sparkutils.ML

class GBTClassificationModel(vanilaModel: VanillaGBTClassificationModel)
    extends StringIndexingWrapperModel[SparkGBTClassificationModel, SparkGBTClassifier](vanilaModel) {

  def this() = this(new VanillaGBTClassificationModel())

}

class VanillaGBTClassificationModel()
    extends SparkModelWrapper[SparkGBTClassificationModel, SparkGBTClassifier]
    with LoadableWithFallback[SparkGBTClassificationModel, SparkGBTClassifier]
    with PredictorParams
    with Logging {

  override private[deeplang] def _transformSchema(schema: StructType): Option[StructType] = {
    val predictionColumnName = $(predictionColumn)
    Some(StructType(schema.fields :+ StructField(predictionColumnName, DoubleType)))
  }

  override val params: Array[Param[_]] =
    Array(featuresColumn, predictionColumn)

  override def report: Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "number of features",
          value = sparkModel.numFeatures,
          description = "Number of features the model was trained on."
        )
      )

    super.report
      .withReportName(s"${this.getClass.getSimpleName} with ${sparkModel.getNumTrees} trees")
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
      .withAdditionalTable(CommonTablesGenerators.decisionTree(sparkModel.treeWeights, sparkModel.trees), 2)
  }

  override protected def transformerName: String = classOf[GBTClassificationModel].getSimpleName

  override def tryToLoadModel(path: String): Option[SparkGBTClassificationModel] =
    ML.ModelLoading.GBTClassification(path)

}
