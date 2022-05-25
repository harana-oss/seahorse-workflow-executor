package io.deepsense.deeplang.doperations

import java.util.UUID

import org.scalatest._

import io.deepsense.commons.utils.Logging
import io.deepsense.deeplang._
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.inout.CsvParameters.ColumnSeparatorChoice
import io.deepsense.deeplang.doperations.inout._
import io.deepsense.deeplang.doperations.readwritedataframe.FileScheme
import io.deepsense.deeplang.utils.DataFrameMatchers

/**
 * This suite shouldn't be executed on its own.
 * It depends on an external standalone spark cluster.
 * It should be executed as a part of [[ClusterDependentSpecsSuite]].
 */
@DoNotDiscover
class InputOutputSpec extends
  FreeSpec with BeforeAndAfter with BeforeAndAfterAll with TestFiles with Logging {

  import DataFrameMatchers._

  implicit lazy val ctx = StandaloneSparkClusterForTests.executionContext

  private val someFormatsSupportedByDriver = Seq(
    new InputFileFormatChoice.Csv()
      .setCsvColumnSeparator(ColumnSeparatorChoice.Comma())
      .setNamesIncluded(true)
      .setShouldConvertToBoolean(true),
    new InputFileFormatChoice.Json()
  )

  private val someFormatsSupportedByCluster =
    someFormatsSupportedByDriver :+ new InputFileFormatChoice.Parquet()

  assume({
    val clusterClasses = someFormatsSupportedByCluster.map(_.getClass).toSet
    val allClasses = InputFileFormatChoice.choiceOrder.toSet
    clusterClasses == allClasses
  }, s"""All formats are supported on cluster - if this assumption no longer
      |holds you probably need to either fix production
      |code and/or add test files or change this test.""".stripMargin
  )

  "Files with" - {
    val schemes = List(FileScheme.File, FileScheme.HTTPS)
    for (fileScheme <- schemes) {
      s"'${fileScheme.pathPrefix}' scheme path of" - {
        for (driverFileFormat <- someFormatsSupportedByDriver) {
          s"$driverFileFormat format work on driver" - {
            for (clusterFileFormat <- someFormatsSupportedByCluster) {
              s"with $clusterFileFormat format works on cluster" in {
                info("Reading file on driver")
                val path = testFile(driverFileFormat, fileScheme)
                val dataframe = read(path, driverFileFormat)

                info("Saving dataframe to HDFS")
                val someHdfsTmpPath = StandaloneSparkClusterForTests.generateSomeHdfsTmpPath()
                write(someHdfsTmpPath, OutputFromInputFileFormat(clusterFileFormat))(dataframe)

                info("Reading dataframe from HDFS back")
                val dataframeReadBackFromHdfs = read(someHdfsTmpPath, clusterFileFormat)
                assertDataFramesEqual(dataframeReadBackFromHdfs, dataframe, checkRowOrder = false)

                info("Writing dataframe back on driver")
                val someDriverTmpPath = generateSomeDriverTmpPath()
                write(
                  someDriverTmpPath,
                  OutputFromInputFileFormat(driverFileFormat)
                )(dataframeReadBackFromHdfs)

                info("Dataframe contains same data after all those operations")
                val finalDataframe = read(someDriverTmpPath, driverFileFormat)
                assertDataFramesEqual(finalDataframe, dataframe, checkRowOrder = false)
              }
            }
          }
        }
      }
    }
  }

  private def generateSomeDriverTmpPath(): String =
    absoluteTestsDirPath.fullPath + "tmp-" + UUID.randomUUID() + ".data"

  private def read(
      path: String,
      fileFormat: InputFileFormatChoice): DataFrame = {
    val readDF = new ReadDataFrame()
      .setStorageType(
        new InputStorageTypeChoice.File()
          .setSourceFile(path)
          .setFileFormat(fileFormat))
    readDF.executeUntyped(Vector.empty[DOperable])(ctx).head.asInstanceOf[DataFrame]
  }

  private def write(path: String, fileFormat: OutputFileFormatChoice)
                   (dataframe: DataFrame): Unit = {
    val write = new WriteDataFrame()
      .setStorageType(
        new OutputStorageTypeChoice.File()
          .setOutputFile(path)
          .setFileFormat(fileFormat)
      )
    write.executeUntyped(Vector(dataframe))(ctx)
  }

}
