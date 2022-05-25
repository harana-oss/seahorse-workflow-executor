package io.deepsense.deeplang.doperations.readwritedataframe.googlestorage

import java.io.{File => _}
import java.util.UUID

import org.apache.spark.sql._

import io.deepsense.commons.utils.Logging
import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperations.inout.{InputFileFormatChoice, InputStorageTypeChoice}
import io.deepsense.deeplang.doperations.readwritedataframe.filestorage.DataFrameFromFileReader
import io.deepsense.deeplang.doperations.readwritedataframe.{FilePath, FileScheme}

object DataFrameFromGoogleSheetReader extends Logging {

  def readFromGoogleSheet(googleSheet: InputStorageTypeChoice.GoogleSheet)
                         (implicit context: ExecutionContext): DataFrame = {
    val id = googleSheet.getGoogleSheetId()
    val tmpPath = FilePath(
      FileScheme.File, s"/tmp/seahorse/google_sheet_${id}__${UUID.randomUUID()}.csv"
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
      ).setSourceFile(tmpPath.fullPath)

    DataFrameFromFileReader.readFromFile(readDownloadedGoogleFileParams)
  }

}
