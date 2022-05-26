package io.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.classification.{RandomForestClassificationModel => SparkRandomForestClassificationModel}
import org.apache.spark.ml.classification.{RandomForestClassifier => SparkRandomForestClassifier}
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

import io.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.ProbabilisticClassifierParams
import io.deepsense.deeplang.doperables.stringindexingwrapper.StringIndexingWrapperModel
import io.deepsense.deeplang.doperables.LoadableWithFallback
import io.deepsense.deeplang.doperables.SparkModelWrapper
import io.deepsense.deeplang.params.Param
import io.deepsense.sparkutils.ML

class RandomForestClassificationModel(vanillaModel: VanillaRandomForestClassificationModel)
    extends StringIndexingWrapperModel[SparkRandomForestClassificationModel, SparkRandomForestClassifier](
      vanillaModel
    ) {

  def this() = this(new VanillaRandomForestClassificationModel())

}

class VanillaRandomForestClassificationModel
    extends SparkModelWrapper[SparkRandomForestClassificationModel, SparkRandomForestClassifier]
    with LoadableWithFallback[SparkRandomForestClassificationModel, SparkRandomForestClassifier]
    with ProbabilisticClassifierParams {

  override private[deeplang] def _transformSchema(schema: StructType): Option[StructType] = {
    val predictionColumnName    = $(predictionColumn)
    val probabilityColumnName   = $(probabilityColumn)
    val rawPredictionColumnName = $(rawPredictionColumn)
    Some(
      StructType(
        schema.fields ++ Seq(
          StructField(predictionColumnName, DoubleType),
          StructField(probabilityColumnName, new io.deepsense.sparkutils.Linalg.VectorUDT),
          StructField(rawPredictionColumnName, new io.deepsense.sparkutils.Linalg.VectorUDT)
        )
      )
    )
  }

  override val params: Array[Param[_]] = Array(featuresColumn, predictionColumn, probabilityColumn, rawPredictionColumn) // thresholds

  override def report: Report = {
    val treeWeight = SparkSummaryEntry(
      name = "tree weights",
      value = sparkModel.treeWeights,
      description = "Weights for each tree."
    )

    super.report
      .withAdditionalTable(CommonTablesGenerators.modelSummary(List(treeWeight)))
  }

  override protected def transformerName: String =
    classOf[RandomForestClassificationModel].getSimpleName

  override def tryToLoadModel(path: String): Option[SparkRandomForestClassificationModel] =
    ML.ModelLoading.randomForestClassification(path)

}
