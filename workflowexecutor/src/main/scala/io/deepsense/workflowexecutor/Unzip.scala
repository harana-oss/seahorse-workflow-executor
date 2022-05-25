package io.deepsense.workflowexecutor

import java.io._
import java.util.zip.ZipInputStream

import scala.reflect.io.Path

import com.google.common.io.Files

import io.deepsense.commons.utils.Logging

object Unzip extends Logging {

  /**
   * Unzips a ZIP file to a temporary directory. Allows to filter extracted files by name.
   * @param inputFile A path to the input file.
   * @param filter A predicate that operates on file names. If true then the file will be
   *               extracted.
   * @return A path where the input archive was extracted.
   */
  def unzipToTmp(inputFile: String, filter: (String) => Boolean): String = {
    val zis: ZipInputStream = new ZipInputStream(new FileInputStream(inputFile))
    val tempDir = Files.createTempDir()
    logger.info(s"Created temporary directory for $inputFile: ${tempDir.getAbsolutePath}")

    var entry = zis.getNextEntry
    while (entry != null) {
      if (filter(entry.getName)) {
        val path = Path(entry.getName)
        val entryFilename = path.name
        val entryDirName = path.parent

        logger.debug("Entry found in jar file: " +
          s"directory: $entryDirName filename: $entryFilename isDirectory: ${entry.isDirectory}")

        val destinationPath = Path(tempDir) / entryDirName
        new File(destinationPath.toURI).mkdirs()
        if (!entry.isDirectory) {
          val target = new File((destinationPath/entryFilename).toURI)
          val fos = new BufferedOutputStream(new FileOutputStream(target, true))
          transferImpl(zis, fos, close = false)
        }
      }
      entry = zis.getNextEntry
    }
    zis.close()
    tempDir.toString
  }

  /**
   * Unzips the entire archive to a temporary directory.
   * @param inputFile A path to the input file.
   * @return A path where the input archive was extracted.
   */
  def unzipAll(inputFile: String): String =
    unzipToTmp(inputFile, _ => true)

  private def transferImpl(in: InputStream, out: OutputStream, close: Boolean): Unit = {
    try {
      val buffer = new Array[Byte](4096)
      def read(): Unit = {
        val byteCount = in.read(buffer)
        if (byteCount >= 0) {
          out.write(buffer, 0, byteCount)
          read()
        }
      }
      read()
      out.close()
    }
    finally {
      if (close) {
        in.close()
      }
    }
  }
}
