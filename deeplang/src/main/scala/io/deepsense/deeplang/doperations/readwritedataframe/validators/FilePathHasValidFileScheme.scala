package io.deepsense.deeplang.doperations.readwritedataframe.validators

import io.deepsense.deeplang.doperations.inout.{InputStorageTypeChoice, OutputStorageTypeChoice}
import io.deepsense.deeplang.doperations.readwritedataframe.FilePath
import io.deepsense.deeplang.doperations.{ReadDataFrame, WriteDataFrame}

object FilePathHasValidFileScheme {

  def validate(wdf: WriteDataFrame): Unit = {
    import OutputStorageTypeChoice._

    wdf.getStorageType() match {
      case file: File =>
        val path = file.getOutputFile()
        FilePath(path)
      case _ =>
    }
  }

  def validate(rdf: ReadDataFrame): Unit = {
    import InputStorageTypeChoice._

    rdf.getStorageType() match {
      case file: File =>
        val path = file.getSourceFile()
        FilePath(path)
      case _ =>
    }
  }

}
