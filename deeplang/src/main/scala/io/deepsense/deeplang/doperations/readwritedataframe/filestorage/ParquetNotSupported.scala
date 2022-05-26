package io.deepsense.deeplang.doperations.readwritedataframe.filestorage

import io.deepsense.deeplang.doperations.readwritedataframe.FileScheme
import io.deepsense.deeplang.exceptions.DeepLangException

case object ParquetNotSupported
    extends DeepLangException({
      val supportedScheme = FileScheme.supportedByParquet.mkString("[", ",", "]")
      s"Parquet file format supported only with $supportedScheme file schemes"
    })
