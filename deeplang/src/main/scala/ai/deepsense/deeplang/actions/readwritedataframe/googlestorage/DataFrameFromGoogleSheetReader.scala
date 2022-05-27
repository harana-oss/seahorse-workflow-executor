package ai.deepsense.deeplang.actions.readwritedataframe.googlestorage

import java.io.{File => _}
import java.util.UUID

import org.apache.spark.sql._

import ai.deepsense.commons.utils.Logging
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actions.inout.InputFileFormatChoice
import ai.deepsense.deeplang.actions.inout.InputStorageTypeChoice
import ai.deepsense.deeplang.actions.readwritedataframe.filestorage.DataFrameFromFileReader
import ai.deepsense.deeplang.actions.readwritedataframe.FilePath
import ai.deepsense.deeplang.actions.readwritedataframe.FileScheme

object DataFrameFromGoogleSheetReader extends Logging {

  def readFromGoogleSheet(
      googleSheet: InputStorageTypeChoice.GoogleSheet
  )(implicit context: ExecutionContext): DataFrame = {
    val id      = googleSheet.getGoogleSheetId()
    val tmpPath = FilePath(
      FileScheme.File,
      s"/tmp/seahorse/google_sheet_${id}__${UUID.randomUUID()}.csv"
    )

    GoogleDriveClient.downloadGoogleSheetAsCsvFile(
      googleSheet.getGoogleServiceAccountCredentials(),
      googleSheet.getGoogleSheetId(),
      tmpPath.pathWithoutScheme
    )

    val readDownloadedGoogleFileParams = new InputStorageTypeChoice.File()
      .setFileFormat(
        new InputFileFormatChoice.Csv()
          .setCsvColumnSeparator(GoogleDriveClient.googleSheetCsvSeparator)
          .setShouldConvertToBoolean(googleSheet.getShouldConvertToBoolean)
          .setNamesIncluded(googleSheet.getNamesIncluded)
      )
      .setSourceFile(tmpPath.fullPath)

    DataFrameFromFileReader.readFromFile(readDownloadedGoogleFileParams)
  }

}
