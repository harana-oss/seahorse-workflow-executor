package io.deepsense.deeplang.doperations

import java.sql.Timestamp

import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import org.scalatest.BeforeAndAfter

import io.deepsense.deeplang.{TestFiles, DeeplangIntegTestSupport}
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.inout._

class WriteReadDataFrameWithDriverFilesIntegSpec
  extends DeeplangIntegTestSupport
  with BeforeAndAfter with TestFiles {

  import DeeplangIntegTestSupport._

  val schema: StructType =
    StructType(Seq(
      StructField("boolean", BooleanType),
      StructField("double", DoubleType),
      StructField("string", StringType)
    ))

  val rows = {
    val base = Seq(
      Row(true, 0.45, "3.14"),
      Row(false, null, "\"testing...\""),
      Row(false, 3.14159, "Hello, world!"),
      // in case of CSV, an empty string is the same as null - no way around it
      Row(null, null, "")
    )
    val repeatedFewTimes = (1 to 10).flatMap(_ => base)
    repeatedFewTimes
  }

  lazy val dataFrame = createDataFrame(rows, schema)

  "WriteDataFrame and ReadDataFrame" should {
    "write and read CSV file" in {
      val wdf =
        new WriteDataFrame()
          .setStorageType(
            new OutputStorageTypeChoice.File()
              .setOutputFile(absoluteTestsDirPath.fullPath + "/test_files")
              .setFileFormat(
                new OutputFileFormatChoice.Csv()
                  .setCsvColumnSeparator(CsvParameters.ColumnSeparatorChoice.Tab())
                  .setNamesIncluded(true)))
      wdf.executeUntyped(Vector(dataFrame))(executionContext)

      val rdf =
        new ReadDataFrame()
          .setStorageType(
            new InputStorageTypeChoice.File()
              .setSourceFile(absoluteTestsDirPath.fullPath + "/test_files")
              .setFileFormat(new InputFileFormatChoice.Csv()
                .setCsvColumnSeparator(CsvParameters.ColumnSeparatorChoice.Tab())
                .setNamesIncluded(true)
                .setShouldConvertToBoolean(true)))
      val loadedDataFrame = rdf.executeUntyped(Vector())(executionContext).head.asInstanceOf[DataFrame]

      assertDataFramesEqual(loadedDataFrame, dataFrame, checkRowOrder = false)
    }

    "write and read JSON file" in {
      val wdf =
        new WriteDataFrame()
          .setStorageType(new OutputStorageTypeChoice.File()
            .setOutputFile(absoluteTestsDirPath.fullPath + "json")
            .setFileFormat(new OutputFileFormatChoice.Json()))

      wdf.executeUntyped(Vector(dataFrame))(executionContext)

      val rdf =
        new ReadDataFrame()
          .setStorageType(new InputStorageTypeChoice.File()
            .setSourceFile(absoluteTestsDirPath.fullPath + "json")
            .setFileFormat(new InputFileFormatChoice.Json()))
      val loadedDataFrame = rdf.executeUntyped(Vector())(executionContext).head.asInstanceOf[DataFrame]

      assertDataFramesEqual(loadedDataFrame, dataFrame, checkRowOrder = false)
    }
  }
}
