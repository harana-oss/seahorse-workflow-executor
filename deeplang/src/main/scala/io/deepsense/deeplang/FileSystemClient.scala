package io.deepsense.deeplang

import java.io._

import org.joda.time.DateTime

trait FileSystemClient {

  /**
   * Checks if file located by the given path exists
   */
  def fileExists(path: String): Boolean

  /**
   * Serializes given object using default java serialization
   * and saves it to a file under the given path
   */
  def saveObjectToFile[T <: Serializable](path: String, instance: T): Unit

  /**
   * Copies file from the local file system to the remote file system.
   */
  def copyLocalFile[T <: Serializable](localFilePath: String, remoteFilePath: String): Unit

  /**
   * Saves content of the given input stream to the file under the given path.
   */
  def saveInputStreamToFile(inputStream: InputStream, destinationPath: String): Unit

  /**
   * Reads content of the file under the given path and uses default java serialization to
   * deserialize it to the instance of a class with the given type.
   */
  def readFileAsObject[T <: Serializable](path: String): T

  /**
   * Returns basic info about file
   */
  def getFileInfo(path: String): Option[FileInfo]

  /**
   * Deletes file or dir under given path.
   */
  def delete(path: String): Unit
}

case class FileInfo(size: Long, modificationTime: DateTime)

object FileSystemClient {
  def replaceLeadingTildeWithHomeDirectory(path: String): String = {
    if (path.startsWith("~/")) {
      path.replaceFirst("~/", System.getProperty("user.home") + "/")
    } else {
      path
    }
  }
}
