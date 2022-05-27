package ai.deepsense.deeplang.actions.readwritedataframe.googlestorage

import java.util.UUID

import ai.deepsense.commons.utils.FileOperations
import ai.deepsense.commons.utils.LoggerForCallerClass
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.inout.OutputStorageTypeChoice.GoogleSheet
import ai.deepsense.deeplang.actions.inout.OutputFileFormatChoice
import ai.deepsense.deeplang.actions.inout.OutputStorageTypeChoice
import ai.deepsense.deeplang.actions.readwritedataframe.filestorage.DataFrameToFileWriter
import ai.deepsense.deeplang.actions.readwritedataframe.FilePath
import ai.deepsense.deeplang.actions.readwritedataframe.FileScheme

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
