package io.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.feature.{StringIndexer => SparkStringIndexer}
import org.apache.spark.ml.feature.{StringIndexerModel => SparkStringIndexerModel}
import spray.json.JsObject
import spray.json.JsString

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.doperables.serialization.JsonObjectPersistence
import io.deepsense.deeplang.doperables.serialization.PathsUtils
import io.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import io.deepsense.deeplang.doperables.MultiColumnModel
import io.deepsense.deeplang.doperables.SparkSingleColumnModelWrapper
import io.deepsense.deeplang.doperables.Transformer
import io.deepsense.deeplang.params.Param

trait StringIndexerModel extends Transformer

case class MultiColumnStringIndexerModel()
    extends MultiColumnModel[SparkStringIndexerModel, SparkStringIndexer, SingleColumnStringIndexerModel]
    with StringIndexerModel {

  override def getSpecificParams: Array[Param[_]] = Array()

  override def report: Report = {
    val tables = models.map(model => model.report.content.tables)
    val name   = s"${this.getClass.getSimpleName} with ${models.length} columns"
    tables
      .foldRight(super.report.withReportName(name))((seqTables, accReport) =>
        seqTables.foldRight(accReport)((t, r) => r.withAdditionalTable(t))
      )
  }

  override def loadTransformer(ctx: ExecutionContext, path: String): this.type =
    this.setModels(loadModels(ctx, path))

  override protected def saveTransformer(ctx: ExecutionContext, path: String): Unit =
    saveModels(ctx, path)

  private def saveModels(ctx: ExecutionContext, path: String): Unit = {
    saveNumberOfModels(ctx, path, models.size)
    val modelsPath = Transformer.modelFilePath(path)
    models.zipWithIndex.foreach { case (model, index) =>
      val modelPath = PathsUtils.combinePaths(modelsPath, index.toString)
      model.save(ctx, modelPath)
    }
  }

  private def saveNumberOfModels(ctx: ExecutionContext, path: String, numberOfModels: Int): Unit = {
    val json = JsObject(
      MultiColumnStringIndexerModel.numberOfModelsKey -> JsString(numberOfModels.toString)
    )
    val numberOfModelsFilePath = MultiColumnStringIndexerModel.numberOfModelsPath(path)
    JsonObjectPersistence.saveJsonToFile(ctx, numberOfModelsFilePath, json)
  }

  private def loadModels(ctx: ExecutionContext, path: String): Seq[SingleColumnStringIndexerModel] = {
    val numberOfModels = loadNumberOfModels(ctx, path)
    val modelsPath     = Transformer.modelFilePath(path)
    (0 until numberOfModels).map { index =>
      val modelPath = PathsUtils.combinePaths(modelsPath, index.toString)
      Transformer.load(ctx, modelPath).asInstanceOf[SingleColumnStringIndexerModel]
    }
  }

  private def loadNumberOfModels(ctx: ExecutionContext, path: String): Int = {
    val numberOfModelsFilePath = MultiColumnStringIndexerModel.numberOfModelsPath(path)
    val json                   = JsonObjectPersistence.loadJsonFromFile(ctx, numberOfModelsFilePath).asJsObject
    json.fields(MultiColumnStringIndexerModel.numberOfModelsKey).convertTo[String].toInt
  }

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String
  ): SerializableSparkModel[SparkStringIndexerModel] =
    throw new UnsupportedOperationException("There is no single model to load for MultiColumnStringIndexerModel")

}

object MultiColumnStringIndexerModel {

  val numberOfModelsKey = "numberOfModels"

  val numberOfModelsFileName = "numberOfModels"

  def numberOfModelsPath(path: String): String =
    PathsUtils.combinePaths(path, numberOfModelsFileName)

}

class SingleColumnStringIndexerModel
    extends SparkSingleColumnModelWrapper[SparkStringIndexerModel, SparkStringIndexer]
    with StringIndexerModel {

  override def getSpecificParams: Array[Param[_]] = Array()

  override def report: Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "labels",
          value = sparkModel.labels,
          description = "Ordered list of labels, corresponding to indices to be assigned."
        )
      )

    super.report
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String
  ): SerializableSparkModel[SparkStringIndexerModel] =
    new SerializableSparkModel(SparkStringIndexerModel.load(path))

}
