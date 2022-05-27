package ai.deepsense.deeplang.actions.exceptions

import ai.deepsense.deeplang.exceptions.FlowException

case class DuplicatedColumnsException(columns: List[String])
    extends FlowException(s"""|DataFrame contains duplicated column names:
                              |${columns.map(col => s"`$col`").mkString("[", ", ", "]")}""".stripMargin)
