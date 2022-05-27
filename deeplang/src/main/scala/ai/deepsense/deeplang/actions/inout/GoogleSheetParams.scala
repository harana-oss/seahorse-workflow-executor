package ai.deepsense.deeplang.actions.inout

import ai.deepsense.deeplang.actions.readwritedataframe.googlestorage._
import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.StringParameter

trait GoogleSheetParams { this: Params =>

  val googleSheetId = StringParameter(name = "Google Sheet Id", description = None)

  def getGoogleSheetId(): GoogleSheetId = $(googleSheetId)

  def setGoogleSheetId(value: GoogleSheetId): this.type = set(googleSheetId, value)

  val serviceAccountCredentials = StringParameter(
    name = "Google Service Account credentials JSON",
    description = Some(
      "Json file representing google service account credentials to be used for accessing " +
        "Google sheet."
    )
  )

  def getGoogleServiceAccountCredentials(): GoogleCretendialsJson = $(serviceAccountCredentials)

  def setGoogleServiceAccountCredentials(value: GoogleCretendialsJson): this.type =
    set(serviceAccountCredentials, value)

}
