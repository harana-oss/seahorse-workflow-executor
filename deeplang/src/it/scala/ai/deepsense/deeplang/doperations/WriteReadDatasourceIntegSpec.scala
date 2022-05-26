package ai.deepsense.deeplang.doperations

import org.scalatest.BeforeAndAfter
import org.scalatest.BeforeAndAfterAll
import org.scalatest.FreeSpec

import ai.deepsense.deeplang.utils.DataFrameMatchers
import ai.deepsense.deeplang.InMemoryDataFrame
import ai.deepsense.deeplang.LocalExecutionContext
import ai.deepsense.deeplang.TestDataSources
import ai.deepsense.deeplang.TestFiles

class WriteReadDatasourceIntegSpec
    extends FreeSpec
    with BeforeAndAfter
    with BeforeAndAfterAll
    with LocalExecutionContext
    with InMemoryDataFrame
    with TestFiles
    with TestDataSources {

  for (ds <- someDatasourcesForWriting) {
    s"`${ds.getParams.getName}` datasource should be readable and writeable" in {
      val wds = WriteDatasource().setDatasourceId(ds.getId)
      wds.execute(inMemoryDataFrame)(context)

      val rds       = ReadDatasource().setDatasourceId(ds.getId)
      val dataframe = rds.execute()(context)

      DataFrameMatchers.assertDataFramesEqual(dataframe, inMemoryDataFrame)
    }
  }

  private val context = LocalExecutionContext.createExecutionContext(datasourceClient)

}
