package io.deepsense.deeplang.doperables

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.param.{BooleanParam, DoubleParam, ParamMap}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame => SparkDataFrame, Dataset}
import org.scalatest.mockito.MockitoSugar

import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import io.deepsense.deeplang.params.wrappers.spark.DoubleParamWrapper
import io.deepsense.deeplang.params.{Param, Params}
import io.deepsense.deeplang.{DeeplangTestSupport, ExecutionContext}
import io.deepsense.sparkutils.ML

object EstimatorModelWrappersFixtures extends MockitoSugar with DeeplangTestSupport {

  trait HasNumericParam extends Params {
    val numericParamWrapper = new DoubleParamWrapper[
        ml.param.Params { val numericParam: ml.param.DoubleParam }](
      "name",
      Some("description"),
      _.numericParam)
    setDefault(numericParamWrapper, 1.0)
  }

  class ExampleSparkEstimatorWrapper
    extends SparkEstimatorWrapper
      [ExampleSparkModel, ExampleSparkEstimator, ExampleSparkModelWrapper]
    with HasNumericParam {

    def setNumericParamWrapper(value: Double): this.type = set(numericParamWrapper, value)

    override def report: Report = ???
    override val params: Array[Param[_]] = Array(numericParamWrapper)
  }

  class ExampleSparkEstimator extends ML.Estimator[ExampleSparkModel] {

    def this(id: String) = this()

    override val uid: String = "estimatorId"

    val numericParam = new DoubleParam(uid, "numeric", "description")

    def setNumericParam(value: Double): this.type = set(numericParam, value)

    override def fitDF(dataset: SparkDataFrame): ExampleSparkModel = {
      require($(numericParam) == paramValueToSet)
      fitModel
    }

    val transformSchemaShouldThrowParam = new BooleanParam(uid, "throwing", "description")
    setDefault(transformSchemaShouldThrowParam -> false)

    def setTransformSchemaShouldThrow(b: Boolean): this.type =
      set(transformSchemaShouldThrowParam, b)

    override def transformSchema(schema: StructType): StructType = {
      if ($(transformSchemaShouldThrowParam)) {
        throw exceptionThrownByTransformSchema
      }
      require($(numericParam) == paramValueToSet)
      transformedSchema
    }

    override def copy(extra: ParamMap): ml.Estimator[ExampleSparkModel] = {
      defaultCopy(extra)
    }
  }

  class ExampleSparkModel extends ML.Model[ExampleSparkModel] {

    override val uid: String = "modelId"

    val numericParam = new DoubleParam(uid, "name", "description")

    def setNumericParam(value: Double): this.type = set(numericParam, value)

    override def copy(extra: ParamMap): ExampleSparkModel =
      extra.toSeq.foldLeft(new ExampleSparkModel())((model, paramPair) => model.set(paramPair))

    override def transformDF(dataset: SparkDataFrame): SparkDataFrame = {
      require($(numericParam) == paramValueToSet)
      fitDataFrame
    }

    override def transformSchema(schema: StructType): StructType = ???
  }

  class ExampleSparkModelWrapper
    extends SparkModelWrapper[ExampleSparkModel, ExampleSparkEstimator]
    with HasNumericParam {

    def setNumericParamWrapper(value: Double): this.type = set(numericParamWrapper, value)

    override def report: Report = ???
    override val params: Array[Param[_]] = Array(numericParamWrapper)

    override protected def loadModel(
      ctx: ExecutionContext,
      path: String): SerializableSparkModel[ExampleSparkModel] = ???
  }

  val fitModel = new ExampleSparkModel()
  val fitDataFrame = createSparkDataFrame()
  val transformedSchema = createSchema()
  val paramValueToSet = 12.0

  val exceptionThrownByTransformSchema = new Exception("mock exception")
}
