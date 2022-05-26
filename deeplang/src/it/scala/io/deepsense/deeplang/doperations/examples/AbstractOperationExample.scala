package io.deepsense.deeplang.doperations.examples

import java.io.File
import java.io.PrintWriter

import io.deepsense.commons.utils.Logging
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.ReadDataFrame
import io.deepsense.deeplang.doperations.inout.CsvParameters.ColumnSeparatorChoice.Comma
import io.deepsense.deeplang.doperations.readwritedataframe.FileScheme
import io.deepsense.deeplang.DOperable
import io.deepsense.deeplang.DOperation
import io.deepsense.deeplang.DeeplangIntegTestSupport

abstract class AbstractOperationExample[T <: DOperation] extends DeeplangIntegTestSupport with Logging {

  def dOperation: T

  final def className: String = dOperation.getClass.getSimpleName

  def fileNames: Seq[String] = Seq.empty

  def loadCsv(fileName: String): DataFrame =
    ReadDataFrame(
      FileScheme.File.pathPrefix + this.getClass.getResource(s"/test_files/$fileName.csv").getPath,
      Comma(),
      csvNamesIncluded = true,
      csvConvertToBoolean = false
    ).executeUntyped(Vector.empty[DOperable])(executionContext)
      .head
      .asInstanceOf[DataFrame]

  def inputDataFrames: Seq[DataFrame] = fileNames.map(loadCsv)

  className should {
    "successfully run execute() and generate example" in {
      val op = dOperation
      val outputDfs = op
        .executeUntyped(inputDataFrames.toVector)(executionContext)
        .collect { case df: DataFrame => df }
      val html =
        ExampleHtmlFormatter.exampleHtml(op, inputDataFrames, outputDfs)

      // TODO Make it not rely on relative path it's run from
      val examplePageFile = new File("docs/operations/examples/" + className + ".md")

      examplePageFile.getParentFile.mkdirs()
      examplePageFile.createNewFile()

      val writer = new PrintWriter(examplePageFile)
      // scalastyle:off println
      writer.println(html)
      // scalastyle:on println
      writer.flush()
      writer.close()
      logger.info("Created doc page for " + className)
    }
  }

}
