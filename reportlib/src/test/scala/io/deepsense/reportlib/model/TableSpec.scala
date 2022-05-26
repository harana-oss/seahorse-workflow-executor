package io.deepsense.reportlib.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import io.deepsense.commons.types.ColumnType

class TableSpec extends AnyWordSpec with Matchers {

  "Table" should {
    "throw IllegalArgumentException" when {
      "created with columnNames and columnTypes of different size" in {
        an[IllegalArgumentException] should be thrownBy
          Table(
            "Name",
            "Description",
            Some(List("col1", "col2")),
            List(ColumnType.string, ColumnType.string, ColumnType.string),
            None,
            List(List(Some("v1"), None, None))
          )
      }
      "created one data row of size different than columnTypes size" in {
        an[IllegalArgumentException] should be thrownBy
          Table(
            "Name",
            "Description",
            Some(List("col1", "col2", "col3")),
            List(ColumnType.string, ColumnType.string, ColumnType.string),
            None,
            List(List(Some("v1"), None))
          )
      }
    }
    "get created" when {
      "no column names are passed" in {
        Table(
          "Name",
          "Description",
          None,
          List(ColumnType.string, ColumnType.string, ColumnType.string),
          None,
          List(List(Some("v1"), None, None))
        )
        info("Table created")
      }
      "no data rows are passed" in {
        Table(
          "Name",
          "Description",
          None,
          List(ColumnType.string, ColumnType.string, ColumnType.string),
          None,
          List()
        )
        info("Table created")
      }
    }
  }

}
