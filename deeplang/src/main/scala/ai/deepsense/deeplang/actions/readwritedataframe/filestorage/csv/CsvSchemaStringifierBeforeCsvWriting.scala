package ai.deepsense.deeplang.actions.readwritedataframe.filestorage.csv

import java.sql.Timestamp

import org.apache.spark.sql.Row
import org.apache.spark.sql.types._

import ai.deepsense.commons.datetime.DateTimeConverter
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.exceptions.UnsupportedColumnTypeException

/** In CSV there are no type hints/formats. Everything is plain text between separators.
  *
  * That's why it's needed to convert all fields to string and make sure that there are no nested structures like Maps
  * or Arrays.
  */
object CsvSchemaStringifierBeforeCsvWriting {

  def preprocess(dataFrame: DataFrame)(implicit context: ExecutionContext): DataFrame = {
    requireNoComplexTypes(dataFrame)

    val schema                                                 = dataFrame.sparkDataFrame.schema
    def stringifySelectedTypes(schema: StructType): StructType = {
      StructType(
        schema.map { case field: StructField => field.copy(dataType = StringType) }
      )
    }

    context.dataFrameBuilder.buildDataFrame(
      stringifySelectedTypes(schema),
      dataFrame.sparkDataFrame.rdd.map(stringifySelectedCells(schema))
    )
  }

  private def requireNoComplexTypes(dataFrame: DataFrame): Unit = {
    dataFrame.sparkDataFrame.schema.fields.map(structField => (structField.dataType, structField.name)).foreach {
      case (dataType, columnName) =>
        dataType match {
          case _: ArrayType | _: MapType | _: StructType =>
            throw UnsupportedColumnTypeException(columnName, dataType)
          case _                                         => ()
        }
    }

  }

  private def stringifySelectedCells(originalSchema: StructType)(row: Row): Row = {
    Row.fromSeq(row.toSeq.zipWithIndex.map { case (value, index) =>
      (value, originalSchema(index).dataType) match {
        case (null, _)          => ""
        case (_, BooleanType)   =>
          if (value.asInstanceOf[Boolean]) "1" else "0"
        case (_, TimestampType) =>
          DateTimeConverter.toString(DateTimeConverter.fromMillis(value.asInstanceOf[Timestamp].getTime))
        case (x, _)             => value.toString
      }
    })
  }

}
