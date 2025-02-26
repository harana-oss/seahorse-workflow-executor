package ai.deepsense.docgen

import java.io.File
import java.io.PrintWriter

import ai.deepsense.deeplang.Action

trait RedirectCreator {

  // scalastyle:off println

  /** @return number of redirects created */
  def createRedirects(sparkOperations: Seq[OperationWithSparkClassName], forceUpdate: Boolean): Int = {

    sparkOperations.map { case OperationWithSparkClassName(operation, sparkClassName) =>
      val redirectFile = new File("docs/uuid/" + operation.id + ".md")
      if (!redirectFile.exists() || forceUpdate) {
        createRedirect(redirectFile, operation, sparkClassName)
        1
      } else
        0
    }.sum
  }

  private def createRedirect(redirectFile: File, operation: Action, sparkClassName: String) = {
    val writer = new PrintWriter(redirectFile)
    writer.println("---")
    writer.println("layout: redirect")
    writer.println("redirect: ../operations/" + DocUtils.underscorize(operation.name) + ".html")
    writer.println("---")
    writer.flush()
    writer.close()
    println("Created redirect for " + operation.name)
  }
  // scalastyle:on println

}
