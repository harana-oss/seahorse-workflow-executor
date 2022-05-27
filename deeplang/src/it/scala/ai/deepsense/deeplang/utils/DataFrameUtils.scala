package ai.deepsense.deeplang.utils

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame

object DataFrameUtils {

  def collectValues(dataFrame: DataFrame, columnName: String): Set[Any] =
    dataFrame.sparkDataFrame.select(columnName).rdd.map(_.get(0)).collect().toSet

}
