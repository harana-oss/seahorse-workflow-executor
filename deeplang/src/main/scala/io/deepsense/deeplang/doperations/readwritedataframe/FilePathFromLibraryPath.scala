package io.deepsense.deeplang.doperations.readwritedataframe

import io.deepsense.deeplang.ExecutionContext

object FilePathFromLibraryPath {

  def apply(path: FilePath)(implicit ctx: ExecutionContext): FilePath = {
    require(path.fileScheme == FileScheme.Library)
    val libraryPath = ctx.libraryPath + "/" + path.pathWithoutScheme
    FilePath(FileScheme.File, libraryPath)
  }

}
