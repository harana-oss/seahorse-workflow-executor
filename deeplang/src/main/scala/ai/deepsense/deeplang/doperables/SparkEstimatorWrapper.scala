package ai.deepsense.deeplang.doperables

import scala.reflect.runtime.universe._

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import ai.deepsense.deeplang.doperables.serialization.Loadable
import ai.deepsense.deeplang.doperables.serialization.ParamsSerialization
import ai.deepsense.deeplang.doperables.serialization.SerializableSparkEstimator
import ai.deepsense.deeplang.params.wrappers.spark.ParamsWithSparkWrappers
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.TypeUtils

/** Wrapper for creating deeplang Estimators from spark.ml Estimators. It is parametrized by model and estimator types,
  * because these entities are tightly coupled.
  *
  * We assume that every ml.Estimator and SparkModelWrapper has a no-arg constructor.
  *
  * @tparam M
  *   Type of wrapped ml.Model
  * @tparam E
  *   Type of wrapped ml.Estimator
  * @tparam MW
  *   Type of used model wrapper
  */
abstract class SparkEstimatorWrapper[M <: ml.Model[M], E <: ml.Estimator[M], MW <: SparkModelWrapper[M, E]](
    implicit val modelWrapperTag: TypeTag[MW],
    implicit val estimatorTag: TypeTag[E]
) extends Estimator[MW]
    with ParamsWithSparkWrappers
    with ParamsSerialization
    with Loadable {

  val serializableEstimator: SerializableSparkEstimator[M, E] = createEstimatorInstance()

  def sparkEstimator: E = serializableEstimator.sparkEstimator

  override private[deeplang] def _fit(ctx: ExecutionContext, dataFrame: DataFrame): MW = {
    val sparkParams =
      sparkParamMap(sparkEstimator, dataFrame.sparkDataFrame.schema)
    val sparkModel  = serializableEstimator.fit(dataFrame.sparkDataFrame, sparkParams)
    createModelWrapperInstance().setModel(sparkModel).setParent(this)
  }

  override private[deeplang] def _fit_infer(maybeSchema: Option[StructType]): MW = {
    // We want to throw validation exceptions here
    validateSparkEstimatorParams(sparkEstimator, maybeSchema)
    createModelWrapperInstance().setParent(this)
  }

  def createEstimatorInstance(): SerializableSparkEstimator[M, E] =
    new SerializableSparkEstimator[M, E](TypeUtils.instanceOfType(estimatorTag))

  def createModelWrapperInstance(): MW = TypeUtils.instanceOfType(modelWrapperTag)

  override def load(ctx: ExecutionContext, path: String): this.type =
    loadAndSetParams(ctx, path)

}
