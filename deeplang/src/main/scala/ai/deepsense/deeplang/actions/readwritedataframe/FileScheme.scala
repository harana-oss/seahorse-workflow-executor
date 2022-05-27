package ai.deepsense.deeplang.actions.readwritedataframe

import ai.deepsense.deeplang.exceptions.DeepLangException

sealed abstract class FileScheme(val scheme: String) {

  def pathPrefix: String = scheme + "://"

}

object FileScheme {

  case object HTTP    extends FileScheme("http")
  case object HTTPS   extends FileScheme("https")
  case object FTP     extends FileScheme("ftp")
  case object HDFS    extends FileScheme("hdfs")
  case object File    extends FileScheme("file")
  case object Library extends FileScheme("library")

  // TODO Autoderive values. There is macro-library for extracting sealed case objects.
  val values = Seq(HTTP, HTTPS, FTP, HDFS, File, Library)

  val supportedByParquet = Seq(HDFS)

  def fromPath(path: String): FileScheme = {
    val matchingFileSchema = values.find(schema => path.startsWith(schema.pathPrefix))
    matchingFileSchema.getOrElse(throw UnknownFileSchemaForPath(path))
  }

}

case class FilePath(fileScheme: FileScheme, pathWithoutScheme: String) {

  def fullPath: String = fileScheme.pathPrefix + pathWithoutScheme

  def verifyScheme(assertedFileScheme: FileScheme): Unit = assert(fileScheme == assertedFileScheme)

}

object FilePath {

  def apply(fullPath: String): FilePath = {
    val schema            = FileScheme.fromPath(fullPath)
    val pathWithoutSchema = fullPath.substring(schema.pathPrefix.length)
    FilePath(schema, pathWithoutSchema)
  }

  def unapply(fullPath: String): Option[(FileScheme, String)] = unapply(FilePath(fullPath))

}

case class UnknownFileSchemaForPath(path: String)
    extends DeepLangException({
      val allSchemes = FileScheme.values.map(_.scheme).mkString("(", ", ", ")")
      s"Unknown file scheme for path $path. Known file schemes: $allSchemes"
    })
