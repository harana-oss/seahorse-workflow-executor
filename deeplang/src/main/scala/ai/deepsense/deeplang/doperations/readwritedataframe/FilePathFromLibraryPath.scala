package ai.deepsense.deeplang.doperations.readwritedataframe

import ai.deepsense.deeplang.ExecutionContext

object FilePathFromLibraryPath {

  def apply(path: FilePath)(implicit ctx: ExecutionContext): FilePath = {
    require(path.fileScheme == FileScheme.Library)
    val libraryPath = ctx.libraryPath + "/" + path.pathWithoutScheme
    FilePath(FileScheme.File, libraryPath)
  }

}
