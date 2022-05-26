package ai.deepsense.deeplang.doperations

import org.scalatest.BeforeAndAfter
import org.scalatest.BeforeAndAfterAll
import org.scalatest.FreeSpec

import ai.deepsense.deeplang.LocalExecutionContext
import ai.deepsense.deeplang.TestDataSources
import ai.deepsense.deeplang.TestFiles

class ReadDatasourceIntegSpec
    extends FreeSpec
    with BeforeAndAfter
    with BeforeAndAfterAll
    with LocalExecutionContext
    with TestDataSources
    with TestFiles {

  for (ds <- someDatasourcesForReading) {
    s"ReadDatasource should work with datasource ${ds.getParams.getName}" in {
      val rds = ReadDatasource().setDatasourceId(ds.getId)
      rds.execute()(LocalExecutionContext.createExecutionContext(datasourceClient))
    }
  }

}
