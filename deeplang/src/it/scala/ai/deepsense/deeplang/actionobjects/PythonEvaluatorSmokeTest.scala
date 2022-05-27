package ai.deepsense.deeplang.actionobjects

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType
import org.mockito.Mockito._

import ai.deepsense.deeplang.parameters.ParamPair

class PythonEvaluatorSmokeTest extends AbstractEvaluatorSmokeTest {

  override def className: String = "PythonEvaluator"

  override val evaluator = new PythonEvaluator()

  override val evaluatorParams: Seq[ParamPair[_]] = Seq()

  override def setUpStubs(): Unit = {
    val someMetric = Seq[Row](Row(1.0))
    val metricDF   = createDataFrame(someMetric, StructType(Seq(StructField("metric", DoubleType, nullable = false))))
    when(executionContext.dataFrameStorage.getOutputDataFrame(0)).thenReturn(Some(metricDF.sparkDataFrame))
  }

}
