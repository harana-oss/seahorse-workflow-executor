package ai.deepsense.deeplang.doperations.readwritedataframe.validators

import ai.deepsense.deeplang.doperations.inout.InputStorageTypeChoice
import ai.deepsense.deeplang.doperations.inout.OutputStorageTypeChoice
import ai.deepsense.deeplang.doperations.readwritedataframe.FilePath
import ai.deepsense.deeplang.doperations.ReadDataFrame
import ai.deepsense.deeplang.doperations.WriteDataFrame

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
