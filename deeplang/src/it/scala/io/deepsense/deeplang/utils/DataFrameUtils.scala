package io.deepsense.deeplang.utils

import io.deepsense.deeplang.doperables.dataframe.DataFrame

object DataFrameUtils {

  def collectValues(dataFrame: DataFrame, columnName: String): Set[Any] =
    dataFrame.sparkDataFrame.select(columnName).rdd.map(_.get(0)).collect().toSet

}
