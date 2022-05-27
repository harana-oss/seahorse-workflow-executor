package ai.deepsense.deeplang.actionobjects

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.param.BooleanParam
import org.apache.spark.ml.param.DoubleParam
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame => SparkDataFrame}
import org.apache.spark.sql.Dataset
import org.scalatestplus.mockito.MockitoSugar

import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actionobjects.serialization.SerializableSparkModel
import ai.deepsense.deeplang.parameters.wrappers.spark.DoubleParameterWrapper
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.DeeplangTestSupport
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.sparkutils.ML

object EstimatorModelWrappersFixtures extends MockitoSugar with DeeplangTestSupport {

  trait HasNumericParam extends Params {

    val numericParamWrapper = new DoubleParameterWrapper[ml.param.Params { val numericParam: ml.param.DoubleParam }](
      "name",
      Some("description"),
      _.numericParam
    )

    setDefault(numericParamWrapper, 1.0)

  }

  class ExampleSparkEstimatorWrapper
      extends SparkEstimatorWrapper[ExampleSparkModel, ExampleSparkEstimator, ExampleSparkModelWrapper]
      with HasNumericParam {

    def setNumericParamWrapper(value: Double): this.type = set(numericParamWrapper, value)

    override def report(extended: Boolean = true): Report = ???

    override val params: Array[Parameter[_]] = Array(numericParamWrapper)

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
      if ($(transformSchemaShouldThrowParam))
        throw exceptionThrownByTransformSchema
      require($(numericParam) == paramValueToSet)
      transformedSchema
    }

    override def copy(extra: ParamMap): ml.Estimator[ExampleSparkModel] =
      defaultCopy(extra)

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

    override def report(extended: Boolean = true): Report = ???

    override val params: Array[Parameter[_]] = Array(numericParamWrapper)

    override protected def loadModel(ctx: ExecutionContext, path: String): SerializableSparkModel[ExampleSparkModel] =
      ???

  }

  val fitModel = new ExampleSparkModel()

  val fitDataFrame = createSparkDataFrame()

  val transformedSchema = createSchema()

  val paramValueToSet = 12.0

  val exceptionThrownByTransformSchema = new Exception("mock exception")

}
