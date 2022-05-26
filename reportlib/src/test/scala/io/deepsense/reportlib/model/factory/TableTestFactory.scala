package io.deepsense.reportlib.model.factory

import io.deepsense.reportlib.model.Table
import io.deepsense.commons.types.ColumnType.ColumnType

trait TableTestFactory {

  def testTableWithLabels(
      columnNames: Option[List[String]],
      columnTypes: List[ColumnType],
      rowNames: Option[List[String]],
      values: List[List[Option[String]]]
  ): Table =
    Table(TableTestFactory.tableName, TableTestFactory.tableDescription, columnNames, columnTypes, rowNames, values)

  def testEmptyTable: Table =
    Table(TableTestFactory.tableName, TableTestFactory.tableDescription, None, List(), None, List())

}

object TableTestFactory extends TableTestFactory {

  val tableName = "table name"

  val tableDescription = "table description"

}
