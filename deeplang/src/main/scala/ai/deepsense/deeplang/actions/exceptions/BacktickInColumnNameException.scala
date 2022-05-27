package ai.deepsense.deeplang.actions.exceptions

import ai.deepsense.deeplang.exceptions.FlowException

case class BacktickInColumnNameException(columns: List[String])
    extends FlowException(s"""|DataFrame contains column names with backticks:
                              |${columns.map(col => s"`$col`").mkString("[", ", ", "]")}""".stripMargin)
