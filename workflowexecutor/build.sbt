import sbtassembly.PathList

name := "deepsense-seahorse-workflowexecutor"

libraryDependencies ++= Dependencies.workflowexecutor

unmanagedClasspath in Runtime += (baseDirectory.value / "conf")

// Include PyExecutor code in assembled uber-jar (under path inside jar: /pyexecutor)
unmanagedResourceDirectories in Compile += { baseDirectory.value / "../python" }

unmanagedResourceDirectories in Compile += { baseDirectory.value / "./rexecutor" }

enablePlugins(DeepsenseBuildInfoPlugin)

buildInfoPackage := "io.deepsense.workflowexecutor.buildinfo"

target in assembly := new File("target")
assemblyJarName in assembly := "workflowexecutor.jar"
