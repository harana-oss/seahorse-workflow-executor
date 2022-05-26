package io.deepsense.workflowexecutor.deeplang.doperables

import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import org.scalatest.matchers.should.Matchers
import spray.json._

import io.deepsense.deeplang._
import io.deepsense.deeplang.doperables.spark.wrappers.transformers.TransformerSerialization
import io.deepsense.deeplang.doperables.spark.wrappers.transformers.TransformerSerialization._
import io.deepsense.deeplang.utils.CustomTransformerFactory
import io.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import io.deepsense.workflowexecutor.executor.InnerWorkflowExecutorImpl

class CustomTransformerIntegSpec extends DeeplangIntegTestSupport with Matchers with TransformerSerialization {

  val columns = Seq(StructField("column1", DoubleType), StructField("column2", StringType))

  def schema: StructType = StructType(columns)

  val row1 = Seq(1.0, "a")

  val row2 = Seq(2.0, "b")

  val row3 = Seq(3.0, "c")

  val row4 = Seq(4.0, "d")

  val row5 = Seq(5.0, "e")

  val row6 = Seq(6.0, "f")

  val row7 = Seq(7.0, "g")

  val row8 = Seq(8.0, "h")

  val row9 = Seq(9.0, "i")

  val row10 = Seq(10.0, "j")

  val data = Seq(row1, row2, row3, row4, row5, row6, row7, row8, row9, row10)

  "CustomTransformer" should {

    "serialize and deserialize" in {
      val jsonFileURI           = getClass.getResource("/customtransformer/innerWorkflow.json").toURI
      val innerWorkflowJson     = scala.io.Source.fromFile(jsonFileURI).mkString.parseJson.asJsObject
      val dOperationsCatalog    = CatalogRecorder.resourcesCatalogRecorder.catalogs.dOperationsCatalog
      val graphReader           = new GraphReader(dOperationsCatalog)
      val innerWorkflowExecutor = new InnerWorkflowExecutorImpl(graphReader)
      val context               = executionContext.copy(innerWorkflowExecutor = innerWorkflowExecutor)

      val customTransformer = CustomTransformerFactory.createCustomTransformer(innerWorkflowExecutor, innerWorkflowJson)
      val dataFrame         = createDataFrame(data.map(Row.fromSeq), schema)

      customTransformer.applyTransformationAndSerialization(tempDir, dataFrame)(context)
    }
  }

}
