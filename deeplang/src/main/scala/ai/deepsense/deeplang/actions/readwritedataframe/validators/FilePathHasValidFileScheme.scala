package ai.deepsense.deeplang.actions.readwritedataframe.validators

import ai.deepsense.deeplang.actions.inout.InputStorageTypeChoice
import ai.deepsense.deeplang.actions.inout.OutputStorageTypeChoice
import ai.deepsense.deeplang.actions.readwritedataframe.FilePath
import ai.deepsense.deeplang.actions.ReadDataFrame
import ai.deepsense.deeplang.actions.WriteDataFrame

object FilePathHasValidFileScheme {

  def validate(wdf: WriteDataFrame): Unit = {
    import OutputStorageTypeChoice._

    wdf.getStorageType() match {
      case file: File =>
        val path = file.getOutputFile()
        FilePath(path)
      case _          =>
    }
  }

  def validate(rdf: ReadDataFrame): Unit = {
    import InputStorageTypeChoice._

    rdf.getStorageType() match {
      case file: File =>
        val path = file.getSourceFile()
        FilePath(path)
      case _          =>
    }
  }

}
