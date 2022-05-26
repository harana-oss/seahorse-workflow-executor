package ai.deepsense.deeplang.documentation

import ai.deepsense.deeplang.DOperation

trait SparkOperationDocumentation extends OperationDocumentation { self: DOperation =>

  private val sparkVersion = org.apache.spark.SPARK_VERSION

  private val sparkDocsUrl = s"https://spark.apache.org/docs/$sparkVersion/"

  protected[this] val docsGuideLocation: Option[String]

  /** Generates Spark's guide section with a link. Used by docgen. */
  override def generateDocs: Option[String] = {
    docsGuideLocation.map { guideLocation =>
      val url = sparkDocsUrl + guideLocation
      s"""|For a comprehensive introduction, see
          |<a target="_blank" href="$url">Spark documentation</a>.""".stripMargin
    }
  }

}
