package ai.deepsense.deeplang.actions.readwritedataframe.filestorage

import ai.deepsense.deeplang.actions.readwritedataframe.FileScheme
import ai.deepsense.deeplang.exceptions.FlowException

case object ParquetNotSupported
    extends FlowException({
      val supportedScheme = FileScheme.supportedByParquet.mkString("[", ",", "]")
      s"Parquet file format supported only with $supportedScheme file schemes"
    })
