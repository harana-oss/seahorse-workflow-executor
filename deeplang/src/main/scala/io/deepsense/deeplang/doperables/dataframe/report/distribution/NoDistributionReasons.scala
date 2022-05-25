package io.deepsense.deeplang.doperables.dataframe.report.distribution

import org.apache.spark.sql.types.DataType

object NoDistributionReasons {

  val TooManyDistinctCategoricalValues = "Too many distinct categorical values"

  val NoData = "No data to calculate distribution"

  val OnlyNulls = "No data to calculate distribution - only nulls"

  val SimplifiedReport = "No distributions for simplified report"

  def NotApplicableForType(dataType: DataType): String =
    s"Distribution not applicable for type ${dataType.typeName}"
}
