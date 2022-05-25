package io.deepsense.deeplang.doperables.wrappers

import org.apache.spark.ml.param.{Param, ParamMap}
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql
import org.apache.spark.sql.types.StructType

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.Transformer
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.params.wrappers.deeplang.ParamWrapper
import io.deepsense.sparkutils.ML

class TransformerWrapper(
    executionContext: ExecutionContext,
    transformer: Transformer)
  extends ML.Model[TransformerWrapper] {

  override def copy(extra: ParamMap): TransformerWrapper = {
    val params = ParamTransformer.transform(extra)
    val transformerCopy = transformer.replicate().set(params: _*)
    new TransformerWrapper(executionContext, transformerCopy)
  }

  override def transformDF(dataset: sql.DataFrame): sql.DataFrame = {
    transformer._transform(executionContext, DataFrame.fromSparkDataFrame(dataset.toDF()))
      .sparkDataFrame
  }

  override def transformSchema(schema: StructType): StructType = {
    transformer._transformSchema(schema).get
  }

  override lazy val params: Array[Param[_]] = {
    transformer.params.map(new ParamWrapper(uid, _))
  }

  override val uid: String = Identifiable.randomUID("TransformerWrapper")
}
