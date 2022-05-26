package io.deepsense.deeplang.doperables.report

import scala.language.reflectiveCalls
import scala.util.Try

import io.deepsense.commons.types.ColumnType
import io.deepsense.deeplang.doperables.report.ReportUtils.formatValues
import io.deepsense.deeplang.doperables.report.ReportUtils.shortenLongTableValues
import io.deepsense.deeplang.params.choice.Choice
import io.deepsense.deeplang.params.ParamMap
import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.utils.SparkTypeConverter.sparkAnyToString
import io.deepsense.reportlib.model._
import io.deepsense.sparkutils.Linalg.DenseMatrix

object CommonTablesGenerators {

  private def paramMapToDescriptionLists(params: ParamMap, namePrefix: String = ""): List[List[Option[String]]] = {
    val descriptionList = params.toSeq
      .flatMap(pair =>
        pair.value match {
          case choice: Choice =>
            Seq(List(Some(namePrefix + pair.param.name), Some(choice), Some(pair.param.description))) ++
              paramMapToDescriptionLists(choice.extractParamMap(), namePrefix)
          case seq: Seq[_] =>
            seq.zipWithIndex.flatMap { case (p: Params, i) =>
              paramMapToDescriptionLists(p.extractParamMap(), s"${pair.param.name} #${i + 1} ")
            }.toList
          case value =>
            Seq(List(Some(namePrefix + pair.param.name), Some(value), Some(pair.param.description)))
        }
      )
      .toList
    shortenLongTableValues(formatValues(descriptionList))
  }

  def params(params: ParamMap): Table = {
    val values = paramMapToDescriptionLists(params)
    Table(
      name = "Parameters",
      description = "Parameters",
      columnNames = Some(List("parameter", "value", "description")),
      columnTypes = List(ColumnType.string, ColumnType.string, ColumnType.string),
      rowNames = None,
      values = shortenLongTableValues(values)
    )
  }

  def modelSummary(tableEntries: List[SummaryEntry]): Table = {
    val values =
      tableEntries.map((entry: SummaryEntry) => List(Some(entry.name), Some(entry.value), Some(entry.description)))

    Table(
      name = "Model Summary",
      description = "Model summary.",
      columnNames = Some(List("result", "value", "description")),
      columnTypes = List(ColumnType.string, ColumnType.string, ColumnType.string),
      rowNames = None,
      values = shortenLongTableValues(values)
    )
  }

  def decisionTree(weights: Array[Double], trees: Array[_ <: { def depth: Int; def numNodes: Int }]): Table = {

    val treesEntries =
      (trees.indices, weights, trees).zipped
        .map((index, weight, tree) => List(Some(index), Some(weight), Some(tree.depth), Some(tree.numNodes)))
        .toList

    Table(
      name = "Decision Trees",
      description = "Decision trees.",
      columnNames = Some(List("tree index", "weight", "depth", "nodes")),
      columnTypes = List(ColumnType.numeric, ColumnType.numeric, ColumnType.numeric, ColumnType.numeric),
      rowNames = None,
      values = shortenLongTableValues(formatValues(treesEntries))
    )
  }

  def categoryMaps(maps: Map[Int, Map[Double, Int]]): Table = {
    val values = maps.toList.map { case (key, value) =>
      List(Some(key), Some(value))
    }

    Table(
      name = "Category Maps",
      description = "Feature value index. Keys are categorical feature indices (column indices). " +
        "Values are maps from original features values to 0-based category indices. " +
        "If a feature is not in this map, it is treated as continuous.",
      columnNames = Some(List("index", "map")),
      columnTypes = List(ColumnType.numeric, ColumnType.string),
      rowNames = None,
      values = shortenLongTableValues(formatValues(values))
    )
  }

  def denseMatrix(name: String, description: String, matrix: DenseMatrix): Table = {
    val (numRows, numCols) = (matrix.numRows, matrix.numCols)
    val values =
      (for (r <- Range(0, numRows)) yield (for (c <- Range(0, numCols)) yield {
        val index = if (!matrix.isTransposed) r + numRows * c else c + numCols * r
        Some(matrix.values(index))
      }).toList).toList

    Table(
      name = name,
      description = description,
      columnNames = Some(List.fill(numCols)("")),
      columnTypes = List.fill(numCols)(ColumnType.numeric),
      rowNames = None,
      values = shortenLongTableValues(formatValues(values))
    )
  }

  trait SummaryEntry {

    def name: String

    def value: String

    def description: String

  }

  case class SparkSummaryEntry(name: String, value: String, description: String) extends SummaryEntry

  object SparkSummaryEntry {

    def apply(name: String, value: => Any, description: String = ""): SummaryEntry = {
      val safeValue = Try(value).getOrElse("N/A")
      SparkSummaryEntry(name, sparkAnyToString(safeValue), description)
    }

  }

}
