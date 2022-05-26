import sbtassembly.PathList

name := "deepsense-seahorse-workflowexecutor"

libraryDependencies ++= Dependencies.workflowexecutor

Runtime / unmanagedClasspath += (baseDirectory.value / "conf")

// Include PyExecutor code in assembled uber-jar (under path inside jar: /pyexecutor)
Compile / unmanagedResourceDirectories += baseDirectory.value / "../python"

Compile / unmanagedResourceDirectories += baseDirectory.value / "./rexecutor"

enablePlugins(DeepsenseBuildInfoPlugin)

buildInfoPackage := "io.deepsense.workflowexecutor.buildinfo"

assembly / target          := new File("target")
assembly / assemblyJarName := "workflowexecutor.jar"
