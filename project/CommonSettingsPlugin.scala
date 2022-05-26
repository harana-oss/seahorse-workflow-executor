import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin.autoImport._
import sbtassembly.PathList

// scalastyle:off

object CommonSettingsPlugin extends AutoPlugin {
  override def trigger = allRequirements

  lazy val OurIT = config("it") extend Test

  lazy val artifactoryUrl = settingKey[String]("Artifactory URL to deploy packages to")

  override def globalSettings = Seq(
    // Set custom URL using -Dartifactory.url
    // sbt -Dartifactory.url=http://192.168.59.104/artifactory/
    artifactoryUrl := sys.props.getOrElse("artifactory.url", "http://artifactory.deepsense.codilime.com:8081/artifactory/"),
    // Default scala version
    scalaVersion := Version.scala
  )

  override def projectSettings = Seq(
    organization := "io.deepsense",
    crossScalaVersions := Seq(Version.scala),
    scalacOptions := Seq(
      "-unchecked", "-deprecation", "-encoding", "utf8", "-feature",
      "-language:existentials", "-language:implicitConversions"
    ),
    javacOptions ++= Seq(
      "-source", Version.java,
      "-target", Version.java
    ),
    // javacOptions are copied to javaDoc and -target is not a valid javaDoc flag.
    javacOptions in doc := Seq(
      "-source", Version.java,
      "-Xdoclint:none" // suppress errors for generated (and other, too) code
    ),
    resolvers ++= Dependencies.resolvers,
    // Disable using the Scala version in output paths and artifacts
    crossPaths := true
  ) ++ ouritSettings ++ testSettings ++ Seq(
    test := (test in Test).value
  ) ++ Seq(
    publishTo := {
      Some(Resolver.file("ds-workflow-executor-ivy-repo", new File( "./target/ds-workflow-executor-ivy-repo" )))
    },
    credentials += Credentials(Path.userHome / ".artifactory_credentials")
  )

  lazy val assemblySettings = Seq(
    // Necessary while assembling uber-jar (omitting MANIFEST.MF file from constituent jar files)
    assemblyMergeStrategy in assembly := {
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
    test in assembly := {}
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
