import sbt.Tests.{SubProcess, Group}
import CommonSettingsPlugin._

// scalastyle:off

name := "deepsense-seahorse-deeplang"

// Integration tests using Spark Clusters need jar
(test in OurIT) := ((test in OurIT).dependsOn (assembly)).value

// Only one spark context per JVM
def assignTestsToJVMs(testDefs: Seq[TestDefinition]) = {
  val (forJvm1, forJvm2) = testDefs.partition(_.name.contains("ClusterDependentSpecsSuite"))

  Seq(
    Group(
      name = "tests_for_jvm_1",
      tests = forJvm1,
      runPolicy = SubProcess(ForkOptions())
    ),
    Group(
      name = "test_for_jvm_2",
      tests = forJvm2,
      runPolicy = SubProcess(ForkOptions())
    )
  )
}

testGrouping in OurIT := {
  val testDefinitions = (definedTests in OurIT).value
  assignTestsToJVMs(testDefinitions)
}

libraryDependencies ++= Dependencies.deeplang

// scalastyle:on
