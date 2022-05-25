package io.deepsense.deeplang.doperations.inout

import io.deepsense.deeplang.doperations.readwritedataframe.googlestorage._
import io.deepsense.deeplang.params.{Params, StringParam}

trait GoogleSheetParams { this: Params =>

  val googleSheetId = StringParam(
    name = "Google Sheet Id",
    description = None)

  def getGoogleSheetId(): GoogleSheetId = $(googleSheetId)
  def setGoogleSheetId(value: GoogleSheetId): this.type = set(googleSheetId, value)

  val serviceAccountCredentials = StringParam(
    name = "Google Service Account credentials JSON",
    description = Some("Json file representing google service account credentials to be used for accessing " +
      "Google sheet."))

  def getGoogleServiceAccountCredentials(): GoogleCretendialsJson = $(serviceAccountCredentials)
  def setGoogleServiceAccountCredentials(value: GoogleCretendialsJson): this.type =
    set(serviceAccountCredentials, value)

}
