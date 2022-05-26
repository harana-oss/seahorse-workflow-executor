package io.deepsense.deeplang.doperations.readwritedataframe.googlestorage

import java.util.UUID

import io.deepsense.commons.utils.FileOperations
import io.deepsense.commons.utils.LoggerForCallerClass
import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.inout.OutputStorageTypeChoice.GoogleSheet
import io.deepsense.deeplang.doperations.inout.OutputFileFormatChoice
import io.deepsense.deeplang.doperations.inout.OutputStorageTypeChoice
import io.deepsense.deeplang.doperations.readwritedataframe.filestorage.DataFrameToFileWriter
import io.deepsense.deeplang.doperations.readwritedataframe.FilePath
import io.deepsense.deeplang.doperations.readwritedataframe.FileScheme

object DataFrameToGoogleSheetWriter {

  val logger = LoggerForCallerClass()

  def writeToGoogleSheet(
      googleSheetChoice: GoogleSheet,
      context: ExecutionContext,
      dataFrame: DataFrame
  ): Unit = {
    val localTmpFile: FilePath = saveDataFrameAsDriverCsvFile(
      googleSheetChoice,
      context,
      dataFrame
    )
    GoogleDriveClient.uploadCsvFileAsGoogleSheet(
      googleSheetChoice.getGoogleServiceAccountCredentials(),
      googleSheetChoice.getGoogleSheetId(),
      localTmpFile.pathWithoutScheme
    )
  }

  private def saveDataFrameAsDriverCsvFile(
      googleSheetChoice: GoogleSheet,
      context: ExecutionContext,
      dataFrame: DataFrame
  ): FilePath = {
    val sheetId = googleSheetChoice.getGoogleSheetId()

    val localTmpFile = FilePath(
      FileScheme.File,
      s"/tmp/seahorse/google_sheet_${sheetId}__${UUID.randomUUID()}.csv"
    )

    FileOperations.mkdirsParents(new java.io.File(localTmpFile.pathWithoutScheme))

    val localTmpFileParams = new OutputStorageTypeChoice.File()
      .setOutputFile(localTmpFile.fullPath)
      .setFileFormat(
        new OutputFileFormatChoice.Csv()
          .setCsvColumnSeparator(GoogleDriveClient.googleSheetCsvSeparator)
          .setNamesIncluded(googleSheetChoice.getNamesIncluded)
      )
    DataFrameToFileWriter.writeToFile(localTmpFileParams, context, dataFrame)
    localTmpFile
  }

}
