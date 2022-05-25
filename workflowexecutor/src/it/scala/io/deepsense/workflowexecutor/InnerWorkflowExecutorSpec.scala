package io.deepsense.workflowexecutor

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{DoubleType, StructField, StructType}
import spray.json._

import io.deepsense.commons.exception.DeepSenseException
import io.deepsense.deeplang.doperables.SqlColumnTransformer
import io.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import io.deepsense.deeplang.doperations._
import io.deepsense.deeplang.doperations.custom.{Sink, Source}
import io.deepsense.deeplang.doperations.spark.wrappers.evaluators.CreateRegressionEvaluator
import io.deepsense.deeplang.params.custom.InnerWorkflow
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection
import io.deepsense.deeplang.{CatalogRecorder, DeeplangIntegTestSupport, InnerWorkflowExecutor, _}
import io.deepsense.graph.{DeeplangGraph, Edge, Node}
import io.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import io.deepsense.models.json.workflow.InnerWorkflowJsonProtocol
import io.deepsense.workflowexecutor.executor.InnerWorkflowExecutorImpl

class InnerWorkflowExecutorSpec
  extends DeeplangIntegTestSupport
  with InnerWorkflowJsonProtocol {

  import DeeplangIntegTestSupport._
  import LocalExecutionContext._

  val sourceNodeId = "2603a7b5-aaa9-40ad-9598-23f234ec5c32"
  val sinkNodeId = "d7798d5e-b1c6-4027-873e-a6d653957418"
  val innerNodeId = "b22bd79e-337d-4223-b9ee-84c2526a1b75"

  val sourceNode = Node(sourceNodeId, Source())
  val sinkNode = Node(sinkNodeId, Sink())

  val innerNodeOperation = {
    val inPlace = NoInPlaceChoice()
      .setOutputColumn("output")
    val single = SingleColumnChoice()
      .setInputColumn(NameSingleColumnSelection("column1"))
      .setInPlace(inPlace)
    val params = SqlColumnTransformer()
      .setFormula("2*x")
      .setSingleOrMultiChoice(single)
      .paramValuesToJson
    new SqlColumnTransformation().setParamsFromJson(params)
  }

  val failingOperation = {
    val inPlace = NoInPlaceChoice()
      .setOutputColumn("output")
    val single = SingleColumnChoice()
      .setInputColumn(NameSingleColumnSelection("does not exist"))
      .setInPlace(inPlace)
    val params = SqlColumnTransformer()
      .setFormula("2*x")
      .setSingleOrMultiChoice(single)
      .paramValuesToJson
    SqlColumnTransformation().setParamsFromJson(params)
  }

  val innerNode = Node(innerNodeId, innerNodeOperation)
  val failingNode = Node(innerNodeId, failingOperation)
  val otherNode = Node(Node.Id.randomId, new CreateRegressionEvaluator())

  val simpleGraph = DeeplangGraph(
    Set(sourceNode, sinkNode, innerNode),
    Set(Edge(sourceNode, 0, innerNode, 0), Edge(innerNode, 0, sinkNode, 0)))

  val disconnectedGraph = DeeplangGraph(
    Set(sourceNode, sinkNode, innerNode),
    Set(Edge(sourceNode, 0, innerNode, 0)))

  val cyclicGraph = DeeplangGraph(
    Set(sourceNode, sinkNode, innerNode),
    Set(Edge(sourceNode, 0, sinkNode, 0), Edge(innerNode, 0, innerNode, 0)))

  val failingGraph = DeeplangGraph(
    Set(sourceNode, sinkNode, failingNode),
    Set(Edge(sourceNode, 0, failingNode, 0), Edge(failingNode, 0, sinkNode, 0)))

  val otherGraph = DeeplangGraph(
    Set(sourceNode, sinkNode, otherNode),
    Set(Edge(sourceNode, 0, sinkNode, 0)))

  val dOperationsCatalog = CatalogRecorder.resourcesCatalogRecorder.catalogs.dOperationsCatalog
  val graphReader = new GraphReader(dOperationsCatalog)
  val executor: InnerWorkflowExecutor = new InnerWorkflowExecutorImpl(graphReader)

  val schema = StructType(List(
    StructField("column1", DoubleType),
    StructField("column2", DoubleType)))

  val rows = Seq(
    Row(1.0, 2.0),
    Row(2.0, 3.0)
  )

  val df = createDataFrame(rows, schema)

  "InnerWorkflowExecutor" should {

    "parse inner workflow json" in {
      val innerWorkflow = InnerWorkflow(simpleGraph, JsObject())
      executor.parse(innerWorkflow.toJson.asJsObject) shouldBe innerWorkflow
    }

    "execute workflow" in {

      val expectedSchema = StructType(List(
        StructField("column1", DoubleType),
        StructField("column2", DoubleType),
        StructField("output", DoubleType)))
      val expectedRows = Seq(
        Row(1.0, 2.0, 2.0),
        Row(2.0, 3.0, 4.0))
      val expected = createDataFrame(expectedRows, expectedSchema)

      val innerWorkflow = InnerWorkflow(simpleGraph, JsObject())
      val transformed = executor.execute(commonExecutionContext, innerWorkflow, df)

      assertDataFramesEqual(transformed, expected)
    }

    "execute workflow with more ready nodes" in {
      val innerWorkflow = InnerWorkflow(otherGraph, JsObject())
      val transformed = executor.execute(commonExecutionContext, innerWorkflow, df)

      assertDataFramesEqual(transformed, df)
    }

    "throw an exception" when {

      "parsing json that is not workflow" in {
        an[Exception] should be thrownBy {
          executor.parse(JsObject("this format is" -> JsString("invalid")))
        }
      }

      "workflow contains cycle" in {
        val innerWorkflow = InnerWorkflow(cyclicGraph, JsObject())
        a[DeepSenseException] should be thrownBy {
          executor.execute(commonExecutionContext, innerWorkflow, df)
        }
      }

      "workflow is not connected" in {
        val innerWorkflow = InnerWorkflow(disconnectedGraph, JsObject())
        a[DeepSenseException] should be thrownBy {
          executor.execute(commonExecutionContext, innerWorkflow, df)
        }
      }

      "workflow execution fails" in {
        val innerWorkflow = InnerWorkflow(failingGraph, JsObject())
        a[DeepSenseException] should be thrownBy {
          executor.execute(commonExecutionContext, innerWorkflow, df)
        }
      }
    }
  }

  val workflowJson =
    """{
      |  "workflow": {
      |    "nodes": [
      |      {
      |        "id": "2603a7b5-aaa9-40ad-9598-23f234ec5c32",
      |        "operation": {
      |          "id": "f94b04d7-ec34-42f7-8100-93fe235c89f8",
      |          "name": "Source"
      |        },
      |        "parameters": {}
      |      }, {
      |        "id": "d7798d5e-b1c6-4027-873e-a6d653957418",
      |        "operation": {
      |          "id": "e652238f-7415-4da6-95c6-ee33808561b2",
      |          "name": "Sink"
      |        },
      |        "parameters": {}
      |      }, {
      |        "id": "b22bd79e-337d-4223-b9ee-84c2526a1b75",
      |        "operation": {
      |          "id": "012876d9-7a72-47f9-98e4-8ed26db14d6d",
      |          "name": "Execute Mathematical Transformation"
      |        },
      |        "parameters": {
      |          "input column alias": "x",
      |          "formula": "2*x",
      |          "input column": {
      |            "type": "column",
      |            "value": "column1"
      |          },
      |          "output column name": "output"
      |        }
      |      }
      |    ],
      |    "connections": [
      |      {
      |        "from":{
      |          "nodeId": "2603a7b5-aaa9-40ad-9598-23f234ec5c32",
      |          "portIndex": 0
      |        },
      |        "to": {
      |          "nodeId": "b22bd79e-337d-4223-b9ee-84c2526a1b75",
      |          "portIndex":0
      |        }
      |      }, {
      |        "from": {
      |          "nodeId": "b22bd79e-337d-4223-b9ee-84c2526a1b75",
      |          "portIndex":0
      |        },
      |        "to": {
      |          "nodeId": "d7798d5e-b1c6-4027-873e-a6d653957418",
      |          "portIndex":0
      |        }
      |      }
      |    ]
      |  },
      |  "thirdPartyData": "{}",
      |  "source": "2603a7b5-aaa9-40ad-9598-23f234ec5c32",
      |  "sink": "d7798d5e-b1c6-4027-873e-a6d653957418"
      |}""".stripMargin
}
