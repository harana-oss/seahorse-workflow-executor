package ai.deepsense.deeplang.params.wrappers.spark

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.doperables.dataframe.DataFrameColumnsGetter
import ai.deepsense.deeplang.params.ColumnSelectorParam
import ai.deepsense.deeplang.params.selections.MultipleColumnSelection

class ColumnSelectorParamWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.StringArrayParam,
    override val portIndex: Int = 0
) extends ColumnSelectorParam(name, description, portIndex)
    with SparkParamWrapper[P, Array[String], MultipleColumnSelection] {

  override def convert(value: MultipleColumnSelection)(schema: StructType): Array[String] =
    DataFrameColumnsGetter.getColumnNames(schema, value).toArray

  override def replicate(name: String): ColumnSelectorParamWrapper[P] =
    new ColumnSelectorParamWrapper[P](name, description, sparkParamGetter, portIndex)

}
