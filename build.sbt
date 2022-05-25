// scalastyle:off

lazy val settingsForPublished = CommonSettingsPlugin.assemblySettings ++
  LicenceReportSettings.settings ++ PublishSettings.enablePublishing
lazy val settingsForNotPublished = CommonSettingsPlugin.assemblySettings ++
  LicenceReportSettings.settings ++ PublishSettings.disablePublishing

lazy val sparkVersion = Version.spark

lazy val sparkUtilsModuleDirectory = sparkVersion match {
  case "1.6.1" => s"sparkutils$sparkVersion"
  case "2.0.0" | "2.0.1" | "2.0.2" => "sparkutils2.0.x"
}
lazy val sparkUtils = project in file (sparkUtilsModuleDirectory) settings settingsForPublished

lazy val rootProject = project.in(file("."))
  .settings(name := "seahorse")
  .settings(PublishSettings.disablePublishing)
  .aggregate(api, sparkUtils, commons, deeplang, docgen, graph, workflowjson, models,reportlib, workflowexecutormqprotocol,
    workflowexecutor)

lazy val api                    = project settings settingsForPublished

lazy val commons                = project dependsOn (api, sparkUtils) settings settingsForPublished

lazy val deeplang               = project dependsOn (
  commons,
  commons % "test->test",
  graph,
  graph % "test->test",
  reportlib,
  reportlib % "test->test") settings settingsForPublished
lazy val docgen                 = project dependsOn (
  deeplang) settings settingsForNotPublished
lazy val graph                  = project dependsOn (
  commons,
  commons % "test->test") settings settingsForPublished
lazy val workflowjson           = project dependsOn (commons, deeplang, graph, models) settings settingsForNotPublished
lazy val models                 = project dependsOn (commons, deeplang, graph) settings settingsForNotPublished
lazy val reportlib              = project dependsOn commons settings settingsForPublished
lazy val workflowexecutormqprotocol = project dependsOn (
  commons,
  commons % "test->test",
  models,
  reportlib % "test->test",
  workflowjson) settings settingsForNotPublished

lazy val sdk                    = project dependsOn (
  deeplang
) settings settingsForPublished

lazy val workflowexecutor       = project dependsOn (
  commons % "test->test",
  deeplang,
  deeplang % "test->test",
  deeplang % "test->it",
  models,
  workflowjson,
  workflowjson % "test -> test",
  sdk,
  workflowexecutormqprotocol,
  workflowexecutormqprotocol % "test -> test") settings settingsForNotPublished

// Sequentially perform integration tests
addCommandAlias("ds-it",
    ";commons/it:test " +
    ";deeplang/it:test " +
    ";graph/it:test " +
    ";workflowjson/it:test " +
    ";models/it:test " +
    ";reportlib/it:test " +
    ";workflowexecutor/it:test" +
    ";workflowexecutormqprotocol/it:test")

addCommandAlias("generateExamples", "deeplang/it:testOnly io.deepsense.deeplang.doperations.examples.*")

// scalastyle:on
