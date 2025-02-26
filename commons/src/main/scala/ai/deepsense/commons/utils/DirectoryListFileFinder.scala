package ai.deepsense.commons.utils

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

import scala.util.Failure
import scala.util.Success
import scala.util.Try

/** Searches for file by scanning the directories on the list and testing the files with a predicate */
abstract class DirectoryListFileFinder(dirsToSearch: Traversable[File]) {

  def listFilesInDirectory(dir: File): Option[Seq[File]] = Option(dir.listFiles())

  import DirectoryListFileFinder._

  /** A predicate that answers the question 'Does the file f is the one described by desc?' */
  def filePredicate(f: File, desc: Option[String]): Boolean

  def findFile(): Try[File] =
    findFile(None)

  def findFile(desc: String): Try[File] =
    findFile(Some(desc))

  def findFile(desc: Option[String]): Try[File] = {
    findPotentialFiles(
      dirsToSearch,
      listFilesInDirectory
      // convert to Try - give a nice message in the exception concerning the dirs, otherwise just Success it
    ).fold(
      dirs =>
        Failure(
          new IOException(s"Unable to list files in dirs: ${dirs.mkString(", ")}")
        ),
      Success[Seq[File]]
    ).flatMap(
      _.find(filePredicate(_, desc))
        .map(Success[File])
        .getOrElse(
          Failure(
            new FileNotFoundException(
              s"Unable to find file ${desc.map(_ + " ").getOrElse("")}" +
                s"in dirs: ${dirsToSearch.mkString(", ")}"
            )
          )
        )
    )
  }

}

object DirectoryListFileFinder {

  type EitherBadDirsOrFiles = Either[Seq[File], Seq[File]]

  def findPotentialFiles(
      dirs: Traversable[File],
      listFilesInDirectory: File => Option[Seq[File]]
  ): EitherBadDirsOrFiles = {
    dirs.map { dir =>
      val files = listFilesInDirectory(dir)

      // if we're unable to list files inside the dir then
      // let's not lose this information by keeping the dir in Left
      files.toRight(dir)
    }.foldLeft(Right(Seq[File]()): EitherBadDirsOrFiles) {
      case (Left(badDirs), Left(badDir)) => Left(badDir +: badDirs)
      case (Left(badDirs), Right(_))     => Left(badDirs)
      case (Right(_), Left(badDir))      => Left(Seq(badDir))
      case (Right(files), Right(files2)) => Right(files ++ files2)
      case _                             => ??? // to silence buggy 2.10 non-exhaustive match warning
    }
  }

}
