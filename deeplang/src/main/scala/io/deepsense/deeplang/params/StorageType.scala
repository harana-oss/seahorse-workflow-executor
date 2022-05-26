package io.deepsense.deeplang.params

/** Supported storage types for reading/writing DataFrames. */
object StorageType extends Enumeration {

  type StorageType = Value

  val FILE = Value("FILE")

  val JDBC = Value("JDBC")

  val GOOGLE_SHEET = Value("GOOGLE_SHEET")

}
