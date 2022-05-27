package ai.deepsense.deeplang.actionobjects.wrappers

import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql.types.StructType
import org.apache.spark.ml
import org.apache.spark.sql

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.Transformer
import ai.deepsense.deeplang.actionobjects.Estimator
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.parameters.wrappers.deeplang.ParamWrapper
import ai.deepsense.sparkutils.ML

class EstimatorWrapper(executionContext: ExecutionContext, estimator: Estimator[Transformer])
    extends ML.Estimator[TransformerWrapper] {

  override def fitDF(dataset: sql.DataFrame): TransformerWrapper = {
    new TransformerWrapper(
      executionContext,
      estimator._fit(executionContext, DataFrame.fromSparkDataFrame(dataset.toDF()))
    )
  }

  override def copy(extra: ParamMap): EstimatorWrapper = {
    val params        = ParamTransformer.transform(extra)
    val estimatorCopy = estimator.replicate().set(params: _*)
    new EstimatorWrapper(executionContext, estimatorCopy)
  }

  override def transformSchema(schema: StructType): StructType =
    schema

  override lazy val params: Array[ml.param.Param[_]] =
    estimator.params.map(new ParamWrapper(uid, _))

  override val uid: String = Identifiable.randomUID("EstimatorWrapper")

}
