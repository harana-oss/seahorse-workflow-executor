package ai.deepsense.deeplang.actionobjects.serialization

import org.apache.hadoop.fs.Path

object PathsUtils {

  def combinePaths(path1: String, path2: String): String =
    new Path(path1, path2).toString

}
