package ai.deepsense.commons.utils

import java.io.File

import scala.collection.JavaConversions._

import com.typesafe.config.Config

object ConfigWithDirListsImplicits {

  implicit class ConfigWithDirLists(val config: Config) {

    def getDirList(path: String): Seq[File] = {
      config
        .getStringList(path)
        .toList
        .map(new File(_))
        .filter(_.isDirectory)
    }

  }

}
