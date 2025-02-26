package ai.deepsense.deeplang.actionobjects

import org.apache.spark.sql
import org.apache.spark.sql.Column
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

import ai.deepsense.commons.types.ColumnType
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.DatetimeDecomposer.TimestampPart
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrameColumnsGetter
import ai.deepsense.deeplang.parameters.choice.Choice
import ai.deepsense.deeplang.parameters.choice.MultipleChoiceParameter
import ai.deepsense.deeplang.parameters.selections.SingleColumnSelection
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.PrefixBasedColumnCreatorParameter
import ai.deepsense.deeplang.parameters.SingleColumnSelectorParameter

/** Operation that is able to take dataframe and split its timestamp column to many columns containing timestamp parts.
  * Client can choose timestamp parts from set: {year, month, day, hour, minutes, seconds} using parameters. Choosing
  * &#36;part value will result in adding new column with name: {original_timestamp_column_name}_&#36;part of
  * IntegerType containing &#36;part value. If a column with that name already exists
  * {original_timestamp_column_name}_&#36;part_N will be used, where N is first not used Int value starting from 1.
  */
case class DatetimeDecomposer() extends Transformer {

  val timestampColumnParam = SingleColumnSelectorParameter(
    name = "timestamp column",
    description = Some("Timestamp column to decompose."),
    portIndex = 0
  )

  def getTimestampColumn: SingleColumnSelection = $(timestampColumnParam)

  def setTimestampColumn(timestampColumn: SingleColumnSelection): this.type =
    set(timestampColumnParam, timestampColumn)

  val timestampPartsParam = MultipleChoiceParameter[TimestampPart](
    name = "parts",
    description = Some("Parts of the date/time to retain.")
  )

  def getTimestampParts: Set[TimestampPart] = $(timestampPartsParam)

  def setTimestampParts(timestampParts: Set[TimestampPart]): this.type =
    set(timestampPartsParam, timestampParts)

  val timestampPrefixParam = PrefixBasedColumnCreatorParameter(
    name = "prefix",
    description = Some("Common prefix for names of created columns.")
  )

  setDefault(timestampPrefixParam, "")

  def getTimestampPrefix: String = $(timestampPrefixParam)

  def setTimestampPrefix(timestampPrefix: String): this.type =
    set(timestampPrefixParam, timestampPrefix)

  override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] =
    Array(timestampColumnParam, timestampPartsParam, timestampPrefixParam)

  override def applyTransform(context: ExecutionContext, dataFrame: DataFrame): DataFrame = {
    DataFrameColumnsGetter.assertExpectedColumnType(
      dataFrame.sparkDataFrame.schema,
      getTimestampColumn,
      ColumnType.timestamp
    )

    val decomposedColumnName: String = dataFrame.getColumnName(getTimestampColumn)

    val newColumns = for {
      range <- DatetimeDecomposer.timestampPartRanges
      if getTimestampParts.contains(range.part)
    } yield timestampUnitColumn(dataFrame.sparkDataFrame, decomposedColumnName, range)

    dataFrame.withColumns(context, newColumns)
  }

  private[this] def timestampUnitColumn(
      sparkDataFrame: sql.DataFrame,
      columnName: String,
      timestampPart: DatetimeDecomposer.TimestampPartRange
  ): Column = {

    val newColumnName = getTimestampPrefix + timestampPart.part.name

    sparkDataFrame(columnName)
      .substr(timestampPart.start, timestampPart.length)
      .as(newColumnName)
      .cast(DoubleType)
  }

  override def applyTransformSchema(schema: StructType): Option[StructType] = {
    DataFrameColumnsGetter.assertExpectedColumnType(schema, getTimestampColumn, ColumnType.timestamp)

    val newColumns = for {
      range <- DatetimeDecomposer.timestampPartRanges
      if getTimestampParts.contains(range.part)
    } yield StructField(getTimestampPrefix + range.part.name, DoubleType)

    val inferredSchema = StructType(schema.fields ++ newColumns)
    Some(inferredSchema)
  }

}

object DatetimeDecomposer {

  import TimestampPart._

  sealed trait TimestampPart extends Choice {

    override val choiceOrder: List[Class[_ <: Choice]] =
      List(classOf[Year], classOf[Month], classOf[Day], classOf[Hour], classOf[Minutes], classOf[Seconds])

  }

  object TimestampPart {

    case class Year() extends TimestampPart {

      override val name: String = "year"

      override val params: Array[Parameter[_]] = Array()

    }

    case class Month() extends TimestampPart {

      override val name: String = "month"

      override val params: Array[Parameter[_]] = Array()

    }

    case class Day() extends TimestampPart {

      override val name: String = "day"

      override val params: Array[Parameter[_]] = Array()

    }

    case class Hour() extends TimestampPart {

      override val name: String = "hour"

      override val params: Array[Parameter[_]] = Array()

    }

    case class Minutes() extends TimestampPart {

      override val name: String = "minutes"

      override val params: Array[Parameter[_]] = Array()

    }

    case class Seconds() extends TimestampPart {

      override val name: String = "seconds"

      override val params: Array[Parameter[_]] = Array()

    }

  }

  private case class TimestampPartRange(part: TimestampPart, start: Int, length: Int)

  private val timestampPartRanges = List(
    TimestampPartRange(Year(), 0, 4),
    TimestampPartRange(Month(), 6, 2),
    TimestampPartRange(Day(), 9, 2),
    TimestampPartRange(Hour(), 12, 2),
    TimestampPartRange(Minutes(), 15, 2),
    TimestampPartRange(Seconds(), 18, 2)
  )

}
