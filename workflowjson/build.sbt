name := "seahorse-executor-workflow-json"

libraryDependencies ++= Dependencies.workflowJson

// Fork to run all test and run tasks in JVM separated from sbt JVM
fork := true
