package ai.deepsense.deeplang.params

/** Supported file formats for reading/writing DataFrames. */
object FileFormat extends Enumeration {

  type FileFormat = Value

  val CSV = Value("CSV")

  val PARQUET = Value("PARQUET")

  val JSON = Value("JSON")

}
