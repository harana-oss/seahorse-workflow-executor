package ai.deepsense.deeplang.doperations.readwritedataframe.filestorage

import ai.deepsense.deeplang.doperations.readwritedataframe.FileScheme
import ai.deepsense.deeplang.exceptions.DeepLangException

case object ParquetNotSupported
    extends DeepLangException({
      val supportedScheme = FileScheme.supportedByParquet.mkString("[", ",", "]")
      s"Parquet file format supported only with $supportedScheme file schemes"
    })
