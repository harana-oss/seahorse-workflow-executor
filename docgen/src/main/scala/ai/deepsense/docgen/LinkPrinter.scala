package ai.deepsense.docgen

import java.io.File

import ai.deepsense.deeplang.catalogs.actions.ActionCategory

trait LinkPrinter {

  // scalastyle:off println

  def printOperationSiteLinks(
                               operationsByCategory: Map[ActionCategory, Seq[OperationWithSparkClassName]],
                               printAll: Boolean
  ): Unit = {
    println("==== Links for operations.md ====")
    printLinksByCategory(operationsByCategory, (url: String, opName: String) => s"* [$opName]($url)", printAll)
  }

  def printOperationMenuLinks(
                               operationsByCategory: Map[ActionCategory, Seq[OperationWithSparkClassName]],
                               printAll: Boolean
  ): Unit = {
    println("==== Links for operationsmenu.html ====")
    printLinksByCategory(
      operationsByCategory,
      (url: String, opName: String) => s"""<li><a href="{{base}}/$url">$opName</a></li>""",
      printAll
    )

  }

  private def printLinksByCategory(
                                    sparkOperationsByCategory: Map[ActionCategory, Seq[OperationWithSparkClassName]],
                                    createLink: (String, String) => String,
                                    printAll: Boolean
  ): Unit = {

    sparkOperationsByCategory.foreach { case (category, opList) =>
      val linksForCategory =
        opList.toList.sortBy(_.op.name).flatMap { case OperationWithSparkClassName(op, sparkClass) =>
          val underscoredName = DocUtils.underscorize(op.name)
          val url             = s"operations/$underscoredName.html"
          val mdFile          = new File(s"docs/operations/$underscoredName.md")
          if (!mdFile.exists() || printAll) Some(createLink(url, op.name)) else None
        }
      if (linksForCategory.nonEmpty) {
        println(category.name)
        linksForCategory.foreach(println(_))
        println()
      }
    }
  }
  // scalastyle:on println

}
