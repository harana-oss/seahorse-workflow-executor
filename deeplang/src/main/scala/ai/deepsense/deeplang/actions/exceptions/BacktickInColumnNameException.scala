package ai.deepsense.deeplang.actions.exceptions

import ai.deepsense.deeplang.exceptions.DeepLangException

case class BacktickInColumnNameException(columns: List[String])
    extends DeepLangException(s"""|DataFrame contains column names with backticks:
                                  |${columns.map(col => s"`$col`").mkString("[", ", ", "]")}""".stripMargin)
