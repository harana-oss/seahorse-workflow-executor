package ai.deepsense.deeplang.doperations.exceptions

import org.apache.spark.sql.types.StructField

case class ValueConversionException(value: String, field: StructField)
    extends DOperationExecutionException(
      "Value \"" + value + "\" can't be converted to a column \"" + field.name + "\" " +
        "type \"" + field.dataType.simpleString + "\"",
      None
    )
