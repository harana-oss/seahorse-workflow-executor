package io.deepsense.deeplang.doperables.report

import io.deepsense.deeplang.utils.SparkTypeConverter

object ReportUtils {

  val StringPreviewMaxLength = 300

  def shortenLongStrings(value: String, maxLength: Int = StringPreviewMaxLength): String =
    if (value.length < maxLength) {
      value
    } else {
      value.take(maxLength) + "..."
    }

  def shortenLongTableValues(
    vals: List[List[Option[String]]],
    maxLength: Int = StringPreviewMaxLength): List[List[Option[String]]] =
    vals.map(_.map {
      case None => None
      case Some(strVal) => Some(ReportUtils.shortenLongStrings(strVal, maxLength))
    }
  )

  def formatCell(cell: Any): String =
    SparkTypeConverter.sparkAnyToString(cell)

  def formatValues(values: List[List[Option[Any]]]): List[List[Option[String]]] =
    values.map(_.map(_.map(formatCell)))
}
