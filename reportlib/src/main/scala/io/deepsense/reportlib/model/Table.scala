package io.deepsense.reportlib.model

import io.deepsense.commons.types.ColumnType.ColumnType

case class Table(
  name: String,
  description: String,
  columnNames: Option[List[String]],
  columnTypes: List[ColumnType],
  rowNames: Option[List[String]],
  values: List[List[Option[String]]]) {
  require(columnNames match {
    case Some(columnNamesList) => columnNamesList.size == columnTypes.size
    case _ => true
  }, "columnNames and columnTypes should have the same size")
  require(values.filter(_.length != columnTypes.length).isEmpty,
    "at least one data row has size different than columnTypes size")
}

object Table {
  val tableType = "table"
}
