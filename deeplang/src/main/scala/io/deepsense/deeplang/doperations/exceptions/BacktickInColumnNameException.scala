package io.deepsense.deeplang.doperations.exceptions

import io.deepsense.deeplang.exceptions.DeepLangException

case class BacktickInColumnNameException(columns: List[String])
    extends DeepLangException(s"""|DataFrame contains column names with backticks:
                                  |${columns.map(col => s"`$col`").mkString("[", ", ", "]")}""".stripMargin)
