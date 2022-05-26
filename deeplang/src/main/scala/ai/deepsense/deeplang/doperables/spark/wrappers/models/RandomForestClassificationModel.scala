package ai.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.classification.{RandomForestClassificationModel => SparkRandomForestClassificationModel}
import org.apache.spark.ml.classification.{RandomForestClassifier => SparkRandomForestClassifier}
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators
import ai.deepsense.deeplang.doperables.report.Report
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.ProbabilisticClassifierParams
import ai.deepsense.deeplang.doperables.stringindexingwrapper.StringIndexingWrapperModel
import ai.deepsense.deeplang.doperables.LoadableWithFallback
import ai.deepsense.deeplang.doperables.SparkModelWrapper
import ai.deepsense.deeplang.params.Param
import ai.deepsense.sparkutils.ML

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

  override protected def applyTransformSchema(schema: StructType): Option[StructType] = {
    val predictionColumnName    = $(predictionColumn)
    val probabilityColumnName   = $(probabilityColumn)
    val rawPredictionColumnName = $(rawPredictionColumn)
    Some(
      StructType(
        schema.fields ++ Seq(
          StructField(predictionColumnName, DoubleType),
          StructField(probabilityColumnName, new ai.deepsense.sparkutils.Linalg.VectorUDT),
          StructField(rawPredictionColumnName, new ai.deepsense.sparkutils.Linalg.VectorUDT)
        )
      )
    )
  }

  override val params: Array[Param[_]] = Array(featuresColumn, predictionColumn, probabilityColumn, rawPredictionColumn) // thresholds

  override def report(extended: Boolean = true): Report = {
    val treeWeight = SparkSummaryEntry(
      name = "tree weights",
      value = sparkModel.treeWeights,
      description = "Weights for each tree."
    )

    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(List(treeWeight)))
  }

  override protected def transformerName: String =
    classOf[RandomForestClassificationModel].getSimpleName

  override def tryToLoadModel(path: String): Option[SparkRandomForestClassificationModel] =
    ML.ModelLoading.randomForestClassification(path)

}
