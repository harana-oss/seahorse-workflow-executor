package io.deepsense.commons.types

import org.apache.spark.sql.types._

import io.deepsense.commons.types.ColumnType._

object SparkConversions {

  def sparkColumnTypeToColumnType(sparkColumnType: DataType): ColumnType =
    sparkColumnType match {
      case _: NumericType                              => ColumnType.numeric
      case _: StringType                               => ColumnType.string
      case _: BooleanType                              => ColumnType.boolean
      case _: TimestampType                            => ColumnType.timestamp
      case _: ArrayType                                => ColumnType.array
      case _: io.deepsense.sparkutils.Linalg.VectorUDT => ColumnType.vector
      case _                                           => ColumnType.other
    }

}
