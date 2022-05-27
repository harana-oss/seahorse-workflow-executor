package ai.deepsense.deeplang.actions.readwritedataframe.filestorage

import ai.deepsense.deeplang.actions.readwritedataframe.FileScheme
import ai.deepsense.deeplang.exceptions.DeepLangException

case object ParquetNotSupported
    extends DeepLangException({
      val supportedScheme = FileScheme.supportedByParquet.mkString("[", ",", "]")
      s"Parquet file format supported only with $supportedScheme file schemes"
    })
