package io.deepsense.deeplang.doperables.spark.wrappers.models

import scala.language.reflectiveCalls

import org.apache.spark.ml.feature.{PCA => SparkPCA}
import org.apache.spark.ml.feature.{PCAModel => SparkPCAModel}

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.SparkSingleColumnModelWrapper
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import io.deepsense.deeplang.params.Param
import io.deepsense.sparkutils.ML

class PCAModel extends SparkSingleColumnModelWrapper[SparkPCAModel, SparkPCA] {

  override protected def getSpecificParams: Array[Param[_]] = Array()

  override def report: Report =
    super.report
      .withAdditionalTable(
        CommonTablesGenerators.denseMatrix(
          name = "A Principal Components Matrix",
          description = "Each column is one principal component.",
          matrix = ML.ModelParams.pcFromPCAModel(sparkModel)
        )
      )

  override protected def loadModel(ctx: ExecutionContext, path: String): SerializableSparkModel[SparkPCAModel] =
    new SerializableSparkModel(SparkPCAModel.load(path))

}
