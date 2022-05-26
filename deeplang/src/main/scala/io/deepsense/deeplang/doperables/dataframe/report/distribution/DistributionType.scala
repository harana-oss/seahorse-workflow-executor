package io.deepsense.deeplang.doperables.dataframe.report.distribution

import org.apache.spark.sql.types._

private[distribution] object DistributionType extends Enumeration {

  type DistributionType = Value

  val Discrete, Continuous, NotApplicable = Value

  def forStructField(structField: StructField): DistributionType = structField.dataType match {
    case TimestampType | DateType | _: NumericType => Continuous
    case StringType | BooleanType                  => Discrete
    case BinaryType | _: ArrayType | _: MapType | _: StructType | _: io.deepsense.sparkutils.Linalg.VectorUDT =>
      NotApplicable
  }

}
