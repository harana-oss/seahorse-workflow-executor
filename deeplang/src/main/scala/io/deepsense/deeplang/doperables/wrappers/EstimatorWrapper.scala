package io.deepsense.deeplang.doperables.wrappers

import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql.types.StructType
import org.apache.spark.ml
import org.apache.spark.sql

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.Transformer
import io.deepsense.deeplang.doperables.Estimator
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.params.wrappers.deeplang.ParamWrapper
import io.deepsense.sparkutils.ML

class EstimatorWrapper(executionContext: ExecutionContext, estimator: Estimator[Transformer])
    extends ML.Estimator[TransformerWrapper] {

  override def fitDF(dataset: sql.DataFrame): TransformerWrapper =
    new TransformerWrapper(
      executionContext,
      estimator._fit(executionContext, DataFrame.fromSparkDataFrame(dataset.toDF()))
    )

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
