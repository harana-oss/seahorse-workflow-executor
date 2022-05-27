package ai.deepsense.deeplang.actions.exceptions

import org.apache.spark.sql.types.StructField

case class ValueConversionException(value: String, field: StructField)
    extends ActionExecutionException(
      "Value \"" + value + "\" can't be converted to a column \"" + field.name + "\" " +
        "type \"" + field.dataType.simpleString + "\"",
      None
    )
