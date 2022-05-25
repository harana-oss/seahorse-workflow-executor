package io.deepsense.reportlib.model

import org.scalatest.{Matchers, WordSpec}
import spray.json._

import io.deepsense.commons.types.ColumnType
import io.deepsense.commons.types.ColumnType.ColumnType
import io.deepsense.commons.types.ColumnType.ColumnType
import io.deepsense.reportlib.model.factory.TableTestFactory

class TableJsonSpec extends WordSpec with Matchers with TableTestFactory with ReportJsonProtocol {

  "Table" should {
    "serialize" when {
      val rowNames: List[String] = List("rowName1", "rowName2")
      val columnNames: List[String] = List("A", "B")
      val columnTypes: List[ColumnType] = List(ColumnType.string, ColumnType.numeric)
      val values: List[List[Option[String]]] = List(List(Some("11"), None), List(None, Some("34")))
      "columnsNames specified" in {
        val json = testTableWithLabels(Some(columnNames), columnTypes, None, values).toJson
        json shouldBe jsonTable(Some(columnNames), columnTypes, None, values)
      }
      "rowsNames specified" in {
        val json = testTableWithLabels(None, columnTypes, Some(rowNames), values).toJson
        json shouldBe jsonTable(None, columnTypes, Some(rowNames), values)
      }
      "rowsNames, columnNames and columTypes specified" in {
        val json = testTableWithLabels(
          Some(columnNames), columnTypes, Some(rowNames), values).toJson
        json shouldBe jsonTable(Some(columnNames), columnTypes, Some(rowNames), values)
      }
      "is empty" in {
        val json = testEmptyTable.toJson
        json shouldBe jsonTable(None, List(), None, List())
      }
    }
    "deserialize" when {
      "filled table" in {
        val columnNames: Some[List[String]] = Some(List("A", "B"))
        val rowNames: Some[List[String]] = Some(List("1", "2"))
        val columnTypes: List[ColumnType] = List(ColumnType.string, ColumnType.numeric)
        val values: List[List[Option[String]]] =
          List(List(Some("a"), Some("1")), List(Some("b"), Some("2")))
        val json = jsonTable(columnNames, columnTypes, rowNames, values)
        json.convertTo[Table] shouldBe testTableWithLabels(
          columnNames, columnTypes, rowNames, values)
      }
      "empty table" in {
        val json = jsonTable(None, List(), None, List())
        json.convertTo[Table] shouldBe testTableWithLabels(None, List(), None, List())
      }
    }
  }

  private def jsonTable(
    columnsNames: Option[List[String]],
    columnTypes: List[ColumnType],
    rowsNames: Option[List[String]],
    values: List[List[Option[String]]]): JsObject = JsObject(Map[String, JsValue](
    "name" -> JsString(TableTestFactory.tableName),
    "description" -> JsString(TableTestFactory.tableDescription),
    "columnNames" -> toJsValue(columnsNames),
    "columnTypes" -> toJsValue(Some(columnTypes.map(_.toString))),
    "rowNames" -> toJsValue(rowsNames),
    "values" ->
      JsArray(
        values.map(row => JsArray(row.map(op => op.map(JsString(_)).getOrElse(JsNull)).toVector))
          .toVector)
  ))

  def toJsValue(values: Option[List[String]]): JsValue with Product with Serializable = {
    values
      .map(values => JsArray(values.map(JsString(_)).toVector)).getOrElse(JsNull)
  }
}
