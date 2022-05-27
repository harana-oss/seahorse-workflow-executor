package ai.deepsense.deeplang.actionobjects.dataframe.report.distribution

import org.apache.spark.sql.types._

private[distribution] object DistributionType extends Enumeration {

  type DistributionType = Value

  val Discrete, Continuous, NotApplicable = Value

  def forStructField(structField: StructField): DistributionType = structField.dataType match {
    case TimestampType | DateType | _: NumericType => Continuous
    case StringType | BooleanType                  => Discrete
    case _                                         => NotApplicable
  }

}
