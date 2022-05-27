package ai.deepsense.deeplang.actionobjects.spark.wrappers.transformers

import org.apache.spark.ml.feature.{VectorAssembler => SparkVectorAssembler}

import ai.deepsense.deeplang.actionobjects.SparkTransformerWrapper
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.selections.MultipleColumnSelection
import ai.deepsense.deeplang.parameters.selections.NameColumnSelection
import ai.deepsense.deeplang.parameters.wrappers.spark.ColumnSelectorParameterWrapper
import ai.deepsense.deeplang.parameters.wrappers.spark.SingleColumnCreatorParameterWrapper

class VectorAssembler extends SparkTransformerWrapper[SparkVectorAssembler] {

  val inputColumns = new ColumnSelectorParameterWrapper[SparkVectorAssembler](
    name = "input columns",
    description = Some("The input columns."),
    sparkParamGetter = _.inputCols,
    portIndex = 0
  )

  val outputColumn = new SingleColumnCreatorParameterWrapper[SparkVectorAssembler](
    name = "output column",
    description = Some("The name of created output column."),
    sparkParamGetter = _.outputCol
  )

  override val params: Array[Parameter[_]] = Array(inputColumns, outputColumn)

  def setInputColumns(selection: Set[String]): this.type =
    set(inputColumns, MultipleColumnSelection(Vector(NameColumnSelection(selection))))

  def setOutputColumn(name: String): this.type =
    set(outputColumn, name)

}
