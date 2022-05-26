package io.deepsense.commons.types

/** Types of data that column in dataframe can have. */
object ColumnType extends Enumeration {

  type ColumnType = Value

  val numeric = Value("numeric")

  val boolean = Value("boolean")

  val string = Value("string")

  val timestamp = Value("timestamp")

  val array = Value("array")

  val vector = Value("vector")

  val other = Value("other")

}
