import scala.util.{Failure, Success, Try}

import sbt.Keys._
import sbt._
import sbtbuildinfo.BuildInfoKey.Entry
import sbtbuildinfo.{BuildInfoKey, BuildInfoPlugin}
import scala.sys.process.Process

object DeepsenseBuildInfoPlugin extends AutoPlugin {
  override def requires: Plugins = BuildInfoPlugin

  def parseVersionString(versionString: String): (Int, Int, Int, String) = {
    // <number>.<number>.<number><optional_rest>
    val splitRegex = """([0-9]+)\.([0-9]+)\.([0-9]+)([^0-9].*)?""".r

    Try {
      versionString match {
        case splitRegex(maj, min, fix, rest) => (maj.toInt, min.toInt, fix.toInt, Option(rest).getOrElse(""))
        case _ => throw new IllegalArgumentException(
          s"Version must conform to regex given by string ${splitRegex.toString()}")
      }
    } match {
      case Success(versionTuple) => versionTuple
      case Failure(nfe: NumberFormatException) =>
        throw new IllegalArgumentException("Version must start with X.Y.Z, " +
          "where X, Y and Z are non negative integers!")

      case Failure(e) => throw e
    }
  }

  val buildInfoKeysSetting: Def.Initialize[Seq[Entry[_]]] = Def.setting {

    lazy val (maj, min, fix, rest) = parseVersionString(version.value)

    Seq(
      BuildInfoKey.action("gitCommitId") {
        Process("git rev-parse HEAD").lineStream.head
      },

      "apiVersionMajor" -> maj,
      "apiVersionMinor" -> min,
      "apiVersionPatch" -> fix,
      "apiVersionRest" -> rest

    )
  }


  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    BuildInfoPlugin.autoImport.buildInfoKeys ++= buildInfoKeysSetting.value
  )
}
