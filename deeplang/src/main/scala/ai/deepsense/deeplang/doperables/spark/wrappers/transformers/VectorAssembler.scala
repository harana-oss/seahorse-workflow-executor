package ai.deepsense.deeplang.doperables.spark.wrappers.transformers

import org.apache.spark.ml.feature.{VectorAssembler => SparkVectorAssembler}

import ai.deepsense.deeplang.doperables.SparkTransformerWrapper
import ai.deepsense.deeplang.params.Param
import ai.deepsense.deeplang.params.selections.MultipleColumnSelection
import ai.deepsense.deeplang.params.selections.NameColumnSelection
import ai.deepsense.deeplang.params.wrappers.spark.ColumnSelectorParamWrapper
import ai.deepsense.deeplang.params.wrappers.spark.SingleColumnCreatorParamWrapper

class VectorAssembler extends SparkTransformerWrapper[SparkVectorAssembler] {

  val inputColumns = new ColumnSelectorParamWrapper[SparkVectorAssembler](
    name = "input columns",
    description = Some("The input columns."),
    sparkParamGetter = _.inputCols,
    portIndex = 0
  )

  val outputColumn = new SingleColumnCreatorParamWrapper[SparkVectorAssembler](
    name = "output column",
    description = Some("The name of created output column."),
    sparkParamGetter = _.outputCol
  )

  override val params: Array[Param[_]] = Array(inputColumns, outputColumn)

  def setInputColumns(selection: Set[String]): this.type =
    set(inputColumns, MultipleColumnSelection(Vector(NameColumnSelection(selection))))

  def setOutputColumn(name: String): this.type =
    set(outputColumn, name)

}
