package ai.deepsense.deeplang.actionobjects.dataframe

import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

object SchemaPrintingUtils {

  def structTypeToString(structType: StructType): String = structType.zipWithIndex.map { case (field, index) =>
    structFieldToString(field, index)
  }.mkString("[", ",", "]")

  def structFieldToString(structField: StructField, index: Int): String =
    structFieldToString(structField, index.toString)

  private def structFieldToString(structField: StructField, index: String): String =
    s"($index -> ${structField.name}: ${structField.dataType})"

}
