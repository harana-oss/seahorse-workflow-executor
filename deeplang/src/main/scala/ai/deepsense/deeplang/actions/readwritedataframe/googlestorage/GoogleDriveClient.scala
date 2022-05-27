package ai.deepsense.deeplang.actions.readwritedataframe.googlestorage

import java.io.ByteArrayInputStream
import java.io.FileOutputStream
import java.util

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.FileContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.model.File
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes

import ai.deepsense.commons.resources.ManagedResource
import ai.deepsense.commons.utils.LoggerForCallerClass
import ai.deepsense.deeplang.actions.inout.CsvParameters.ColumnSeparatorChoice

private[googlestorage] object GoogleDriveClient {

  val logger = LoggerForCallerClass()

  val googleSheetCsvSeparator = ColumnSeparatorChoice.Comma()

  private val ApplicationName = "Seahorse"

  private val Scopes = util.Arrays.asList(DriveScopes.DRIVE)

  def uploadCsvFileAsGoogleSheet(
      credentials: GoogleCretendialsJson,
      sheetId: GoogleSheetId,
      filePath: String
  ): Unit = {
    val fileMetadata = new File().setMimeType("application/vnd.google-apps.spreadsheet")
    val mediaContent = new FileContent("text/csv", new java.io.File(filePath))

    driveService(credentials).files.update(sheetId, fileMetadata, mediaContent).execute
  }

  def downloadGoogleSheetAsCsvFile(
      credentials: GoogleCretendialsJson,
      sheetId: GoogleSheetId,
      filePath: String
  ): Unit = {
    val file = new java.io.File(filePath)
    file.getParentFile.mkdirs()

    ManagedResource(new FileOutputStream(file)) { fos =>
      driveService(credentials).files().export(sheetId, "text/csv").executeMediaAndDownloadTo(fos)
      logger.info(s"Downloaded google sheet id=$sheetId to the file $filePath")
    }
  }

  private def driveService(serviceAccountCredentials: String): Drive = {
    val credential = {
      val in = new ByteArrayInputStream(serviceAccountCredentials.getBytes)
      GoogleCredential.fromStream(in).createScoped(Scopes)
    }
    new Drive.Builder(
      GoogleNetHttpTransport.newTrustedTransport(),
      jsonFactory,
      credential
    ).setApplicationName(ApplicationName).build
  }

  // Default choice is JacksonFactory. However spark depends on Jackson as well
  // and google/spark jackson versions are binary incompatible with each other.
  private val jsonFactory = GsonFactory.getDefaultInstance

}
