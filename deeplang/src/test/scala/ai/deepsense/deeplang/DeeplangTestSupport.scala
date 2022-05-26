package ai.deepsense.deeplang

import org.apache.spark.sql
import org.apache.spark.sql.types.StructType
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar

import ai.deepsense.deeplang.catalogs.doperable.DOperableCatalog
import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import ai.deepsense.deeplang.inference.InferContext

trait DeeplangTestSupport extends MockitoSugar {

  protected def createInferContext(dOperableCatalog: DOperableCatalog): InferContext = MockedInferContext(
    dOperableCatalog
  )

  protected def createExecutionContext: ExecutionContext = {
    val mockedExecutionContext = mock[ExecutionContext]
    val mockedInferContext     = mock[InferContext]
    when(mockedExecutionContext.inferContext).thenReturn(mockedInferContext)
    mockedExecutionContext
  }

  protected def createSchema(fields: Array[String] = Array[String]()): StructType = {
    val schemaMock = mock[StructType]
    when(schemaMock.fieldNames).thenReturn(fields)
    schemaMock
  }

  protected def createSparkDataFrame(schema: StructType = createSchema()) = {
    val sparkDataFrameMock = mock[sql.DataFrame]
    when(sparkDataFrameMock.schema).thenReturn(schema)
    when(sparkDataFrameMock.toDF).thenReturn(sparkDataFrameMock)
    sparkDataFrameMock
  }

  protected def createDataFrame(fields: Array[String] = Array[String]()): DataFrame = {
    val schema = createSchema(fields)
    createDataFrame(schema)
  }

  protected def createDataFrame(schema: StructType): DataFrame = {
    val sparkDataFrameMock = createSparkDataFrame(schema)
    val dataFrameMock      = mock[DataFrame]
    when(dataFrameMock.sparkDataFrame).thenReturn(sparkDataFrameMock)
    when(dataFrameMock.schema).thenReturn(Some(schema))
    dataFrameMock
  }

}
