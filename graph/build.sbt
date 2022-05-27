name := "seahorse-executor-graph"

libraryDependencies ++= Dependencies.graph

// Fork to run all test and run tasks in JVM separated from sbt JVM
fork := false
