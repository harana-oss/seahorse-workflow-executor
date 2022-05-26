package io.deepsense.deeplang.doperations

import org.scalatest.BeforeAndAfter
import org.scalatest.BeforeAndAfterAll
import org.scalatest.freespec.AnyFreeSpec

import io.deepsense.deeplang.utils.DataFrameMatchers
import io.deepsense.deeplang.InMemoryDataFrame
import io.deepsense.deeplang.LocalExecutionContext
import io.deepsense.deeplang.TestDataSources
import io.deepsense.deeplang.TestFiles

class WriteReadDatasourceIntegSpec
    extends AnyFreeSpec
    with BeforeAndAfter
    with BeforeAndAfterAll
    with LocalExecutionContext
    with InMemoryDataFrame
    with TestFiles
    with TestDataSources {

  for (ds <- someDatasourcesForWriting)
    s"`${ds.getParams.getName}` datasource should be readable and writeable" in {
      val wds = WriteDatasource().setDatasourceId(ds.getId)
      wds.execute(inMemoryDataFrame)(context)

      val rds       = ReadDatasource().setDatasourceId(ds.getId)
      val dataframe = rds.execute()(context)

      DataFrameMatchers.assertDataFramesEqual(dataframe, inMemoryDataFrame)
    }

  private val context = LocalExecutionContext.createExecutionContext(datasourceClient)

}
