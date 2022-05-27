package ai.deepsense.deeplang.actionobjects.multicolumn

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrameColumnsGetter

trait SingleColumnTransformerUtils {

  def transformSingleColumnInPlace(
      inputColumn: String,
      dataFrame: DataFrame,
      executionContext: ExecutionContext,
      transform: (String) => DataFrame
  ): DataFrame = {
    val temporaryColumnName =
      DataFrameColumnsGetter.uniqueSuffixedColumnName(inputColumn)
    val temporaryDataFrame  = transform(temporaryColumnName)
    val allColumnNames      = temporaryDataFrame.sparkDataFrame.schema.map(_.name)
    val filteredColumns     = allColumnNames.collect {
      case columnName if columnName == inputColumn         =>
        temporaryDataFrame.sparkDataFrame(temporaryColumnName).as(inputColumn)
      case columnName if columnName != temporaryColumnName =>
        temporaryDataFrame.sparkDataFrame(columnName)
    }

    val filteredDataFrame = temporaryDataFrame.sparkDataFrame.select(filteredColumns: _*)
    DataFrame.fromSparkDataFrame(filteredDataFrame)
  }

}

object SingleColumnTransformerUtils extends SingleColumnTransformerUtils
