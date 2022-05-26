package io.deepsense.commons.utils

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

import scala.util.Success

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DirectoryListFileFinderSpec extends AnyWordSpec with Matchers {

  val uutName: String = DirectoryListFileFinder.getClass.getSimpleName.filterNot(_ == '$')

  trait Setup {

    val fileA = new File("a")

    val fileB = new File("b")

    val fileC = new File("c")

    val theNeedleFileName = "theNeedle"

    val hayStackFileName = "hay.exe"

    val theNeedleFile = new File(theNeedleFileName)

    val hayStackFile = new File(hayStackFileName)

    val goodDirs = Set(fileA, fileB)

    val emptyDirs = Set(fileB)

    val badDirs = Set(fileC)

    val mockFileSystem: Map[File, Seq[File]] = Map(
      fileA -> Seq(theNeedleFile, hayStackFile),
      fileB -> Seq()
    )

    def createUUT(dirList: Traversable[File]): DirectoryListFileFinder = new DirectoryListFileFinder(dirList) {

      override def listFilesInDirectory(dir: File): Option[Seq[File]] = mockFileSystem.get(dir)

      override def filePredicate(f: File, desc: Option[String]): Boolean = f.getName == theNeedleFileName

    }

  }

  s"A $uutName" should {
    "find the files that fulfill the predicate" in {
      new Setup {
        val uut: DirectoryListFileFinder = createUUT(goodDirs)

        uut.findFile() shouldBe Success(theNeedleFile)
      }
    }

    "fail" when {
      "file is not found" in {
        new Setup {
          val uut: DirectoryListFileFinder = createUUT(emptyDirs)

          uut.findFile().failed.get shouldBe a[FileNotFoundException]
        }
      }

      "cannot enter a directory" in {
        new Setup {
          val uut: DirectoryListFileFinder = createUUT(badDirs)

          uut.findFile().failed.get shouldBe an[IOException]
        }
      }

      "given an empty list of directories" in {
        new Setup {
          val uut: DirectoryListFileFinder = createUUT(Seq())

          uut.findFile().failed.get shouldBe a[FileNotFoundException]
        }
      }
    }
  }

}
