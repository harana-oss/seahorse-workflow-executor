package ai.deepsense.deeplang.actionobjects.spark.wrappers.models

import scala.language.reflectiveCalls

import org.apache.spark.ml.feature.{PCA => SparkPCA}
import org.apache.spark.ml.feature.{PCAModel => SparkPCAModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.SparkSingleColumnModelWrapper
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actionobjects.serialization.SerializableSparkModel
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.sparkutils.ML

class PCAModel extends SparkSingleColumnModelWrapper[SparkPCAModel, SparkPCA] {

  override protected def getSpecificParams: Array[Parameter[_]] = Array()

  override def report(extended: Boolean = true): Report = {
    super
      .report(extended)
      .withAdditionalTable(
        CommonTablesGenerators.denseMatrix(
          name = "A Principal Components Matrix",
          description = "Each column is one principal component.",
          matrix = ML.ModelParams.pcFromPCAModel(sparkModel)
        )
      )
  }

  override protected def loadModel(ctx: ExecutionContext, path: String): SerializableSparkModel[SparkPCAModel] =
    new SerializableSparkModel(SparkPCAModel.load(path))

}
