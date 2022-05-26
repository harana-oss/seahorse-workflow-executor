package ai.deepsense.commons.utils

import java.io.File

object FileOperations {

  def mkdirsParents(f: File) =
    f.getParentFile.mkdirs()

  def deleteRecursively(f: File): Boolean = {
    if (f.isFile)
      f.delete()
    else {
      f.listFiles.map(deleteRecursively)
      f.delete()
    }
  }

  def deleteRecursivelyIfExists(f: File): Boolean = {
    if (f.exists) {
      if (f.isFile)
        f.delete()
      else {
        f.listFiles.map(deleteRecursivelyIfExists)
        f.delete()
      }
    } else
      true
  }

}
