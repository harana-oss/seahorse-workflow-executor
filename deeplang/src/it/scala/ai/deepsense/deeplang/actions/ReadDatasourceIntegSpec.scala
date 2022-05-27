package ai.deepsense.deeplang.actions

import org.scalatest.BeforeAndAfter
import org.scalatest.BeforeAndAfterAll
import org.scalatest.freespec.AnyFreeSpec

import ai.deepsense.deeplang.LocalExecutionContext
import ai.deepsense.deeplang.TestDataSources
import ai.deepsense.deeplang.TestFiles

class ReadDatasourceIntegSpec
    extends AnyFreeSpec
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
