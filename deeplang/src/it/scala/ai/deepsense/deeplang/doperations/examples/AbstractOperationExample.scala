package ai.deepsense.deeplang.doperations.examples

import java.io.File
import java.io.PrintWriter

import ai.deepsense.commons.utils.Logging
import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import ai.deepsense.deeplang.doperations.ReadDataFrame
import ai.deepsense.deeplang.doperations.inout.CsvParameters.ColumnSeparatorChoice.Comma
import ai.deepsense.deeplang.doperations.readwritedataframe.FileScheme
import ai.deepsense.deeplang.DOperable
import ai.deepsense.deeplang.DOperation
import ai.deepsense.deeplang.DeeplangIntegTestSupport

abstract class AbstractOperationExample[T <: DOperation] extends DeeplangIntegTestSupport with Logging {

  def dOperation: T

  final def className: String = dOperation.getClass.getSimpleName

  def fileNames: Seq[String] = Seq.empty

  def loadCsv(fileName: String): DataFrame = {
    ReadDataFrame(
      FileScheme.File.pathPrefix + this.getClass.getResource(s"/test_files/$fileName.csv").getPath,
      Comma(),
      csvNamesIncluded = true,
      csvConvertToBoolean = false
    ).executeUntyped(Vector.empty[DOperable])(executionContext)
      .head
      .asInstanceOf[DataFrame]
  }

  def inputDataFrames: Seq[DataFrame] = fileNames.map(loadCsv)

  className should {
    "successfully run execute() and generate example" in {
      val op        = dOperation
      val outputDfs = op
        .executeUntyped(inputDataFrames.toVector)(executionContext)
        .collect { case df: DataFrame => df }
      val html      =
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
