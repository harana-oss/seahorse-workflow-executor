package ai.deepsense.workflowexecutor

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType
import spray.json._

import ai.deepsense.commons.exception.HaranaException
import ai.deepsense.deeplang.actionobjects.SqlColumnTransformer
import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import ai.deepsense.deeplang.actions._
import ai.deepsense.deeplang.actions.custom.Sink
import ai.deepsense.deeplang.actions.custom.Source
import ai.deepsense.deeplang.actions.spark.wrappers.evaluators.CreateRegressionEvaluator
import ai.deepsense.deeplang.parameters.custom.InnerWorkflow
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.CatalogRecorder
import ai.deepsense.deeplang.DeeplangIntegTestSupport
import ai.deepsense.deeplang.InnerWorkflowExecutor
import ai.deepsense.deeplang._
import ai.deepsense.graph.FlowGraph
import ai.deepsense.graph.Edge
import ai.deepsense.graph.Node
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import ai.deepsense.models.json.workflow.InnerWorkflowJsonProtocol
import ai.deepsense.workflowexecutor.executor.InnerWorkflowExecutorImpl

class InnerWorkflowExecutorSpec extends DeeplangIntegTestSupport with InnerWorkflowJsonProtocol {

  import DeeplangIntegTestSupport._
  import LocalExecutionContext._

  val sourceNodeId = "2603a7b5-aaa9-40ad-9598-23f234ec5c32"

  val sinkNodeId   = "d7798d5e-b1c6-4027-873e-a6d653957418"

  val innerNodeId  = "b22bd79e-337d-4223-b9ee-84c2526a1b75"

  val sourceNode = Node(sourceNodeId, Source())

  val sinkNode   = Node(sinkNodeId, Sink())

  def innerNodeOperation = {
    val inPlace = NoInPlaceChoice()
      .setOutputColumn("output")
    val single = SingleColumnChoice()
      .setInputColumn(NameSingleColumnSelection("column1"))
      .setInPlace(inPlace)
    val params = SqlColumnTransformer()
      .setFormula("2*x")
      .setSingleOrMultiChoice(single)
      .paramValuesToJson
    new SqlColumnTransformation().setParamsFromJson(params, graphReader)
  }

  def failingOperation = {
    val inPlace = NoInPlaceChoice()
      .setOutputColumn("output")
    val single = SingleColumnChoice()
      .setInputColumn(NameSingleColumnSelection("does not exist"))
      .setInPlace(inPlace)
    val params = SqlColumnTransformer()
      .setFormula("2*x")
      .setSingleOrMultiChoice(single)
      .paramValuesToJson
    SqlColumnTransformation().setParamsFromJson(params, graphReader)
  }

  val innerNode   = Node(innerNodeId, innerNodeOperation)

  val failingNode = Node(innerNodeId, failingOperation)

  val otherNode   = Node(Node.Id.randomId, new CreateRegressionEvaluator())

  val simpleGraph = FlowGraph(
    Set(sourceNode, sinkNode, innerNode),
    Set(Edge(sourceNode, 0, innerNode, 0), Edge(innerNode, 0, sinkNode, 0))
  )

  val disconnectedGraph = FlowGraph(Set(sourceNode, sinkNode, innerNode), Set(Edge(sourceNode, 0, innerNode, 0)))

  val cyclicGraph = FlowGraph(
    Set(sourceNode, sinkNode, innerNode),
    Set(Edge(sourceNode, 0, sinkNode, 0), Edge(innerNode, 0, innerNode, 0))
  )

  val failingGraph = FlowGraph(
    Set(sourceNode, sinkNode, failingNode),
    Set(Edge(sourceNode, 0, failingNode, 0), Edge(failingNode, 0, sinkNode, 0))
  )

  val otherGraph = FlowGraph(Set(sourceNode, sinkNode, otherNode), Set(Edge(sourceNode, 0, sinkNode, 0)))

  val dOperationsCatalog              = CatalogRecorder.resourcesCatalogRecorder.catalogs.operations

  val graphReader                     = new GraphReader(dOperationsCatalog)

  val executor: InnerWorkflowExecutor = new InnerWorkflowExecutorImpl(graphReader)

  val schema = StructType(List(StructField("column1", DoubleType), StructField("column2", DoubleType)))

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

      val expectedSchema = StructType(
        List(StructField("column1", DoubleType), StructField("column2", DoubleType), StructField("output", DoubleType))
      )
      val expectedRows = Seq(Row(1.0, 2.0, 2.0), Row(2.0, 3.0, 4.0))
      val expected     = createDataFrame(expectedRows, expectedSchema)

      val innerWorkflow = InnerWorkflow(simpleGraph, JsObject())
      val transformed   = executor.execute(commonExecutionContext, innerWorkflow, df)

      assertDataFramesEqual(transformed, expected)
    }

    "execute workflow with more ready nodes" in {
      val innerWorkflow = InnerWorkflow(otherGraph, JsObject())
      val transformed   = executor.execute(commonExecutionContext, innerWorkflow, df)

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
        a[HaranaException] should be thrownBy {
          executor.execute(commonExecutionContext, innerWorkflow, df)
        }
      }

      "workflow is not connected" in {
        val innerWorkflow = InnerWorkflow(disconnectedGraph, JsObject())
        a[HaranaException] should be thrownBy {
          executor.execute(commonExecutionContext, innerWorkflow, df)
        }
      }

      "workflow execution fails" in {
        val innerWorkflow = InnerWorkflow(failingGraph, JsObject())
        a[HaranaException] should be thrownBy {
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
