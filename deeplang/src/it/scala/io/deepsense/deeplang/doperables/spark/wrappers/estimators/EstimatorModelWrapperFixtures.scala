package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import scala.language.reflectiveCalls

import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.ml
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.param.{Param => SparkParam}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import io.deepsense.deeplang.doperables.SparkEstimatorWrapper
import io.deepsense.deeplang.doperables.SparkModelWrapper
import io.deepsense.deeplang.params.wrappers.spark.SingleColumnCreatorParamWrapper
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.Params
import io.deepsense.sparkutils.ML

object EstimatorModelWrapperFixtures {

  class SimpleSparkModel private[EstimatorModelWrapperFixtures] () extends ML.Model[SimpleSparkModel] {

    def this(x: String) = this()

    override val uid: String = "modelId"

    val predictionCol = new SparkParam[String](uid, "name", "description")

    def setPredictionCol(value: String): this.type = set(predictionCol, value)

    override def copy(extra: ParamMap): this.type = defaultCopy(extra)

    override def transformDF(dataset: DataFrame): DataFrame =
      dataset.selectExpr("*", "1 as " + $(predictionCol))

    @DeveloperApi
    override def transformSchema(schema: StructType): StructType = ???

  }

  class SimpleSparkEstimator extends ML.Estimator[SimpleSparkModel] {

    def this(x: String) = this()

    override val uid: String = "estimatorId"

    val predictionCol = new SparkParam[String](uid, "name", "description")

    override def fitDF(dataset: DataFrame): SimpleSparkModel =
      new SimpleSparkModel().setPredictionCol($(predictionCol))

    override def copy(extra: ParamMap): ML.Estimator[SimpleSparkModel] = defaultCopy(extra)

    @DeveloperApi
    override def transformSchema(schema: StructType): StructType =
      schema.add(StructField($(predictionCol), IntegerType, nullable = false))

  }

  trait HasPredictionColumn extends Params {

    val predictionColumn =
      new SingleColumnCreatorParamWrapper[ml.param.Params { val predictionCol: SparkParam[String] }](
        "prediction column",
        None,
        _.predictionCol
      )

    setDefault(predictionColumn, "abcdefg")

    def getPredictionColumn(): String = $(predictionColumn)

    def setPredictionColumn(value: String): this.type = set(predictionColumn, value)

  }

  class SimpleSparkModelWrapper
      extends SparkModelWrapper[SimpleSparkModel, SimpleSparkEstimator]
      with HasPredictionColumn {

    override val params: Array[Param[_]] = Array(predictionColumn)

    override def report: Report = ???

    override protected def loadModel(ctx: ExecutionContext, path: String): SerializableSparkModel[SimpleSparkModel] =
      ???

  }

  class SimpleSparkEstimatorWrapper
      extends SparkEstimatorWrapper[SimpleSparkModel, SimpleSparkEstimator, SimpleSparkModelWrapper]
      with HasPredictionColumn {

    override val params: Array[Param[_]] = Array(predictionColumn)

    override def report: Report = ???

  }

}
