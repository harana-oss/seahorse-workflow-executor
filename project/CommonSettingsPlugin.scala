import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin.autoImport._
import sbtassembly.PathList

// scalastyle:off

object CommonSettingsPlugin extends AutoPlugin {
  override def trigger = allRequirements

  lazy val OurIT = config("it") extend Test

  override def globalSettings = Seq(
    // Default scala version
    scalaVersion := Version.scala
  )

  override def projectSettings = Seq(
    organization := "ai.deepsense",
    crossScalaVersions := Seq(Version.scala),
    scalacOptions := Seq(
      "-unchecked", "-deprecation", "-encoding", "utf8", "-feature",
      "-language:existentials", "-language:implicitConversions", "-Xfatal-warnings"
    ),
    javacOptions ++= Seq(
      "-source", Version.java,
      "-target", Version.java
    ),
    // javacOptions are copied to javaDoc and -target is not a valid javaDoc flag.
    doc / javacOptions := Seq(
      "-source", Version.java,
      "-Xdoclint:none" // suppress errors for generated (and other, too) code
    ),
    resolvers ++= Dependencies.resolvers,
    // Disable using the Scala version in output paths and artifacts
    crossPaths := true
  ) ++ ouritSettings ++ testSettings ++ Seq(
    test := (Test / test).value
  )

  lazy val assemblySettings = Seq(
    // Necessary while assembling uber-jar (omitting MANIFEST.MF file from constituent jar files)
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", "MANIFEST.MF")               => MergeStrategy.discard
      case PathList("META-INF", "INDEX.LIST")                => MergeStrategy.discard
      case PathList("META-INF", "ECLIPSEF.SF")               => MergeStrategy.discard
      case PathList("META-INF", "ECLIPSEF.RSA")              => MergeStrategy.discard
      case PathList("META-INF", "DUMMY.SF")                  => MergeStrategy.discard
      case PathList("META-INF", "services", "org.apache.hadoop.fs.FileSystem") => MergeStrategy.concat
      case "reference.conf"                                  => MergeStrategy.concat
      case _ => MergeStrategy.first
    },
    // Skip test while assembling uber-jar
    assembly / test := {}
  )

  lazy val ouritSettings = inConfig(OurIT)(Defaults.testSettings) ++ inConfig(OurIT) {
    Seq(
      testOptions ++= Seq(
        // Show full stacktraces (F), Put results in test-reports
        Tests.Argument(TestFrameworks.ScalaTest, "-oF", "-u", s"target/test-reports-${Version.spark}")
      ),
      javaOptions := Seq(s"-DlogFile=${name.value}", "-Xmx2G", "-Xms2G"),
      fork := true,
      unmanagedClasspath += baseDirectory.value / "conf"
    )
  }

  lazy val testSettings = inConfig(Test) {
    Seq(
      testOptions := Seq(
        // Put results in test-reports
        Tests.Argument(
          TestFrameworks.ScalaTest,
          "-o",
          "-u", s"target/test-reports-${Version.spark}"
        )
      ),
      fork := true,
      javaOptions := Seq(s"-DlogFile=${name.value}"),
      unmanagedClasspath += baseDirectory.value / "conf"
    )
  }

  override def projectConfigurations = OurIT +: super.projectConfigurations
}

// scalastyle:on
