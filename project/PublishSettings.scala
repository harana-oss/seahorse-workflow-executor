import com.jsuereth.sbtpgp.PgpKeys
import sbt.Keys._
import sbt._

// scalastyle:off

object PublishSettings {
  def enablePublishing = Seq(
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  Test / publishArtifact := false,
  pomIncludeRepository := { _ => false },
  pomExtra := (
    <url>https://github.com/deepsense-ai/seahorse</url>
      <licenses>
        <license>
          <name>Apache 2.0</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0</url>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:deepsense.ai/seahorse.git</url>
        <connection>scm:git:git@github.com:deepsense.ai/seahorse.git</connection>
      </scm>
      <developers>
        <developer>
          <name>deepsense.ai</name>
          <email>contact@deepsense.ai</email>
          <url>https://deepsense.ai/</url>
          <organization>deepsense.ai</organization>
          <organizationUrl>https://deepsense.ai/</organizationUrl>
        </developer>
      </developers>))

  def disablePublishing = Seq(
    PgpKeys.publishSigned := ()
  )
}
