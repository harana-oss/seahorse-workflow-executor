package io.deepsense.deeplang.doperations

import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FreeSpec}

import io.deepsense.deeplang.utils.DataFrameMatchers
import io.deepsense.deeplang.{InMemoryDataFrame, LocalExecutionContext, TestDataSources, TestFiles}

class WriteReadDatasourceIntegSpec
  extends FreeSpec with BeforeAndAfter with BeforeAndAfterAll
    with LocalExecutionContext with InMemoryDataFrame with TestFiles with TestDataSources {

  for (ds <- someDatasourcesForWriting) {
    s"`${ds.getParams.getName}` datasource should be readable and writeable" in {
      val wds = WriteDatasource().setDatasourceId(ds.getId)
      wds.execute(inMemoryDataFrame)(context)

      val rds = ReadDatasource().setDatasourceId(ds.getId)
      val dataframe = rds.execute()(context)

      DataFrameMatchers.assertDataFramesEqual(dataframe, inMemoryDataFrame)
    }
  }

  private val context = LocalExecutionContext.createExecutionContext(datasourceClient)

}
