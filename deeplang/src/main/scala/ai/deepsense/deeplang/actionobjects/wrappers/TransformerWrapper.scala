package ai.deepsense.deeplang.actionobjects.wrappers

import org.apache.spark.ml.param.Param
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.Transformer
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.parameters.wrappers.deeplang.ParamWrapper
import ai.deepsense.sparkutils.ML

class TransformerWrapper(executionContext: ExecutionContext, transformer: Transformer)
    extends ML.Model[TransformerWrapper] {

  override def copy(extra: ParamMap): TransformerWrapper = {
    val params          = ParamTransformer.transform(extra)
    val transformerCopy = transformer.replicate().set(params: _*)
    new TransformerWrapper(executionContext, transformerCopy)
  }

  override def transformDF(dataset: sql.DataFrame): sql.DataFrame =
    transformer._transform(executionContext, DataFrame.fromSparkDataFrame(dataset.toDF())).sparkDataFrame

  override def transformSchema(schema: StructType): StructType =
    transformer._transformSchema(schema).get

  override lazy val params: Array[Param[_]] =
    transformer.params.map(new ParamWrapper(uid, _))

  override val uid: String = Identifiable.randomUID("TransformerWrapper")

}
