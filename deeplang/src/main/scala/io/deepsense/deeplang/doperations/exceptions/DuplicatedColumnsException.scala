package io.deepsense.deeplang.doperations.exceptions

import io.deepsense.deeplang.exceptions.DeepLangException

case class DuplicatedColumnsException(columns: List[String])
  extends DeepLangException(
    s"""|DataFrame contains duplicated column names:
        |${columns.map(col => s"`$col`").mkString("[", ", ", "]")}""".stripMargin)
